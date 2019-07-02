package com.xmcc.mll_order.rabbitmq;

import com.xmcc.mll_common.common.Constant;
import com.xmcc.mll_common.common.MessageStatus;
import com.xmcc.mll_common.orderDto.MllOrderDTO;
import com.xmcc.mll_common.rabbit.MllMessage;
import com.xmcc.mll_common.util.DateUtil;
import com.xmcc.mll_common.util.JsonUtil;
import com.xmcc.mll_order.mapper.MllMessageMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;

import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Component
@Slf4j
public class RabbitSenderUtil {


    //springboot自动注入的 rabbitmq的实例
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private MessageConverter messageConverter;

    @Autowired
    private MllMessageMapper mllMessageMapper;

    // 定义过期时间 这个要根据业务来指定
    public static final long EXPIRE = 60*1000;


    //定义confirm 确认监听回调函数  是否成功将消息 发送到rabbitmq的broker 都会执行该方法
    final RabbitTemplate.ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback() {
        //先看第二个参数 就是消息是否发送到了broker的结果
        //第一个参数为传递的消息的信息 包含了全局唯一id
        @Override
        public void confirm(CorrelationData correlationData, boolean flag, String cause) {
            log.info("confirm消息的id为：{}",correlationData.getId());
            log.info("confirm消息的结果为：{}",flag);
            //发送失败 消息没到rabbit broker
            if (!flag){
                //修改消息状态 为发送异常
                mllMessageMapper.updateStatusById(Long.valueOf(correlationData.getId()), MessageStatus.SEND_ERROR.getCode());
            }

        }
    };

    //定义return的监听回调函数  不能将消息路由到了指定queue 就会指定该回调方法
    final RabbitTemplate.ReturnCallback returnCallback = new RabbitTemplate.ReturnCallback() {
        /**
         * @param message   发送的消息
         * @param relayCode 错误码
         * @param relayText 错误文本信息
         * @param exchange  发送的交换机
         * @param routingKey 发送的路由键
         */
        @Override
        public void returnedMessage(Message message, int relayCode, String relayText,
                                    String exchange, String routingKey) {
            message.getMessageProperties().getMessageId();
        }
    };

    // 因为调用的方法是加了事务的 然后抛出异常事务会回滚 这儿加入业务代码的事务
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void orderErrorToProduct(Long id, MllOrderDTO mllOrderDTO){
        //将该条消息的全局id设置进入CorrelationData方便confirm中取出来 唯一标识该消息
        CorrelationData correlationData = new CorrelationData(String.valueOf(id));
        MessageProperties messageProperties = new MessageProperties();
        //设置messageId 在消费端取出来可以判断 重复消费
        messageProperties.setMessageId(String.valueOf(id));
        Message message = messageConverter.toMessage(mllOrderDTO, messageProperties);
        //加入消息确认模式的监听
        rabbitTemplate.setConfirmCallback(confirmCallback);
        //加入消息return模式的监听
        rabbitTemplate.setReturnCallback(returnCallback);


        // 将消息插入数据库
        try{
            log.info("插入消息消息到数据库");
            // 标记消息状态为成功
            int insert = mllMessageMapper.insert(MllMessage.builder().messageId(id)
                    .messageBody(JsonUtil.object2string(mllOrderDTO))
                    .expire(DateUtil.expire(EXPIRE))
                    .status(MessageStatus.SEND_SUCCESS.getCode()).build());
            log.info("插入数据库结果为：{}",insert);
            //如果出现主键冲突异常 那么就是修改次数及时间即可
        }catch (DuplicateKeyException e){
            log.info("插入异常，进行修改");
            //增加次数 与修改过期时间
            mllMessageMapper.updateSendCount(id,DateUtil.expire(EXPIRE),MessageStatus.SEND_SUCCESS.getCode() );

        }




        //1.交换机名称  2.routingKey 3.发送的消息数据 4.该消息的信息 例如全局唯一的id设置6
        rabbitTemplate.convertAndSend(Constant.OrderProduct.ORDER_TO_PRODUCT_EXCHANGE,
                Constant.OrderProduct.ORDER_TO_PRODUCT_ROLLBACK, message,correlationData);
    }

    //消息转换器 使用同一类转换器 方便对象传输
    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }

}
