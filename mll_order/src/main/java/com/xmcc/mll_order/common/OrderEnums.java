package com.xmcc.mll_order.common;

import com.xmcc.mll_common.result.ResultEnums;
import lombok.Getter;

@Getter
public enum OrderEnums implements ResultEnums {

    NO_PAY(1,"未付款"),
    OVER_PAY(2,"已付款"),
    NO_SEND(3,"未发货"),
    OVER_SEND(4,"已发货"),
    SUCCESS(5,"交易成功"),
    CLOSE(6,"交易关闭"),
    WAIT_ASSESS(7,"待评价"),
    ERROR(100,"订单异常"),
    PAY_ERROR(101,"订单支付异常"),
    PRODUCT_STOCK_ERROR(102,"商品库存扣减异常"),
    SEND_GOODS_ERROR(103,"发货异常"),
    //状态很多 不一一写完了
    ;

    private int code;
    private String msg;

    OrderEnums(int code,String msg){
        this.code = code;
        this.msg = msg;
    }

}
