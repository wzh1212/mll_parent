package com.xmcc.mll_miaosha.common;

import com.xmcc.mll_common.result.ResultEnums;
import lombok.Getter;

@Getter
public enum MiaoshaOrderEnums implements ResultEnums {

    NOT_ENOUGH(0,"库存不足"),
    FAIL(1,"您已经秒杀过该商品！");

    private int code;
    private String msg;

    MiaoshaOrderEnums(int code,String msg){
        this.code = code;
        this.msg = msg;
    }
}
