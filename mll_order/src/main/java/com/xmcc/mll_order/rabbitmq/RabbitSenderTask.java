package com.xmcc.mll_order.rabbitmq;

import com.xmcc.mll_common.common.MessageStatus;
import com.xmcc.mll_common.orderDto.MllOrderDTO;
import com.xmcc.mll_common.rabbit.MllMessage;
import com.xmcc.mll_common.util.JsonUtil;
import com.xmcc.mll_order.mapper.MllMessageMapper;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 定时任务
 */
@Component
@EnableScheduling
@Slf4j
public class RabbitSenderTask {

    @Autowired
    private MllMessageMapper mllMessageMapper;

    @Autowired
    private RabbitSenderUtil rabbitSenderUtil;


    // 5 秒执行一次
    @Scheduled(cron = "*/5 * * * * ?")
    public void orderErrorToProductTask(){
        log.info("定时任务执行");
        //这里如果更细的话  还可以状态为发送成功+过期时间进行判断
        List<MllMessage> mllMessageList = mllMessageMapper.queryByStatus(MessageStatus.SEND_ERROR.getCode());
        for (MllMessage mllMessage : mllMessageList){
            // 查看消息发送次数
            if (mllMessage.getSendCount() >= 3){
                //打印错误日志
                log.error("在订单业务失败通过rabbitmq通知商品微服务回滚操作中，消息发送失败次数超过三次，消息id：{}" +
                        "，消息内容：{}，消息发送次数为：{}",mllMessage.getMessageId(),mllMessage.getMessageBody(),mllMessage.getSendCount());
                //修改状态为失败 失败就是另外一套逻辑了 这个时候一般都需要人工干预了
                mllMessageMapper.updateStatusById(mllMessage.getMessageId(),MessageStatus.FAIL.getCode());
                continue;
            }
            //转换为对象 再次发送消息
            MllOrderDTO mllOrderDTO = JsonUtil.string2object(mllMessage.getMessageBody(), new TypeReference<MllOrderDTO>() { });
            rabbitSenderUtil.orderErrorToProduct(mllMessage.getMessageId(),mllOrderDTO);
        }

    }








}
