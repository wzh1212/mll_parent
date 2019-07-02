package com.xmcc.mll_common.common;

public class Constant {

    public interface OrderProduct{
        //订单发送消息到商品微服务的交换机
        String ORDER_TO_PRODUCT_EXCHANGE = "order_to_product_exchange";
        //订单发送消息到商品微服务的队列
        String ORDER_TO_PRODUCT_QUEUE = "order_to_product_queue";
        //订单发送消息到商品微服务的路由键
        String ORDER_TO_PRODUCT_ROLLBACK = "order_to_product.rollback";
        //监听的路由键
        String ORDER_PRODUCT_ROUTINGKEY = "order_to_product.#";
        //交换机类型
        String TOPIC  = "topic";
    }

}
