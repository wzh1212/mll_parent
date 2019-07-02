package com.xmcc.mll_common.rabbit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MllMessage implements Serializable{

    //消息全局id
    private long messageId;
    //消息体
    private String messageBody;
    //消息过期时间
    private Date expire;
    //消息状态 1.已发送 2.已接收 3.已完成 4.消息发送异常 5.消息消费异常 6.失败
    private int status;
    //消息重发次数
    private int sendCount=0;
    //消息消费次数
    private int consumeCount=0;

    // 消费端状态 默认为 0
    private int consumeStatus = 0;



}
