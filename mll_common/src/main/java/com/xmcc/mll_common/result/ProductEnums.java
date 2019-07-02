package com.xmcc.mll_common.result;

import lombok.Getter;

@Getter
public enum ProductEnums implements ResultEnums{

    PRODUCT_NOT_EXISTS(0,"该商品 id 错误"),
    STOCK_NOT_ENOUGH(1,"该商品的库存不足");

    private int code;
    private String msg;

    ProductEnums(int code,String msg){
        this.code = code;
        this.msg = msg;
    }
}
