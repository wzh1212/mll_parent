package com.xmcc.mll_product.rabbitmq;

import com.rabbitmq.client.Channel;
import com.xmcc.mll_common.common.Constant;
import com.xmcc.mll_common.common.MessageStatus;
import com.xmcc.mll_common.orderDto.MllOrderDTO;
import com.xmcc.mll_product.mapper.MllMessageMapper;
import com.xmcc.mll_product.service.MllProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.IOException;


/**
 * 监听类
 */
@Component
@Slf4j
public class OrderProductListener {

    @Autowired
    private MllProductService mllProductService;

    @Autowired
    private MllMessageMapper mllMessageMapper;

    //监听的队列配置。监听到消息后，交给 @RabbitHandler修饰的方法处理
    @RabbitListener(
            bindings = @QueueBinding(
                    //申明队列
                    value=@Queue(value = Constant.OrderProduct.ORDER_TO_PRODUCT_QUEUE,durable = "true"),
                    //申明交换机,指定主题模式
                    exchange=@Exchange(value=Constant.OrderProduct.ORDER_TO_PRODUCT_EXCHANGE,durable = "true",type = Constant.OrderProduct.TOPIC
                            //忽略一些基本的申明时会出现的异常
                            ,ignoreDeclarationExceptions = "true"),
                    //路由键申明 topic模式 使用#表示任意多个
                    key=Constant.OrderProduct.ORDER_PRODUCT_ROUTINGKEY
            )

    )
    @RabbitHandler
    public void receiverMessage(Message message , MllOrderDTO mllOrderDTO, Channel channel) throws IOException {
//        log.info("获得的消息的id为：{}",message.getMessageProperties().getMessageId());
//        log.info("获得消息的内容为：{}",mllOrderDTO);
//        //首先取得消息的编号DELIVERY_TAG
//        long deliveryTag = message.getMessageProperties().getDeliveryTag();
//        log.info("获得消息的DELIVERY_TAG为：{}",deliveryTag);
//        //手动确认
//        channel.basicAck(deliveryTag,false);
//
//        // 事务回滚
//        mllProductService.stockRollback(mllOrderDTO);


        //获得消息id
        String messageId = message.getMessageProperties().getMessageId();
        log.info("接收到消息，消息的id为：{}",messageId);
        //修改接收状态 与发送状态都为消息已接收
        int result = mllMessageMapper.updateConsumerAndStatus(messageId, MessageStatus.GET_SUCCESS.getCode());
        log.info("修改结果为：{}",result);
        //首先取得消息的编号DELIVERY_TAG
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        if(result==1) {
            //消费成功
            try {
                //调用业务方法
                mllProductService.stockRollback(mllOrderDTO);
                //修改消费状态与发送状态为完成
                mllMessageMapper.updateConsumerAndStatusSuccess(messageId, MessageStatus.FINISH.getCode());
            }catch (Exception e){
                //如果出现异常
                //修改状态为消费异常 给其它消费者机会，这儿是结合 发送端定时任务的过期时间 然后会再次发送消息
                //todo:注：定时时间判断是让同学们自己完成的
                mllMessageMapper.updateConsumerAndStatus(messageId, MessageStatus.GET_ERROR.getCode());
                //打印错误日志 抛出异常
                log.error("在商品微服务中，消费订单传递的消息出现异常，异常信息为：{}，消息id为：{}",e,messageId);
                //消费异常也回馈给rabbitmq删除消息 然后发送端重新发送即可
                channel.basicAck(deliveryTag,false);
                throw new  RuntimeException(e);
            }
        }else{
            //如果消息已经被接收 有可能是发送端重复发送 但是这边已经消费成功了
            //这儿可以在发送端判断消息是否发送成功 然后不重复发送 也可以在接收端将查询消费状态
            //根据消费状态去修改发送状态 如果消费状态为3说明已经消费成功  就把发送状态修改了 避免定时任务重复发送
            mllMessageMapper.updateStatusByConsumerStatus(messageId,MessageStatus.FINISH.getCode());
        }
        //如果 result==0 说明不能消费 回馈ack   如果调用业务方法没有异常也是回馈ACK
        //注意，不管是否消费成功我们都应该给服务器回信
        channel.basicAck(deliveryTag,false);

    }


    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }



}
