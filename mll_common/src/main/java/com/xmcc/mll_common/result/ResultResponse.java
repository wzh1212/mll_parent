package com.xmcc.mll_common.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultResponse<T> {
    private int code;
    private String msg;

    private T data;
    //失败了或者成功了 有时候不需要返回 data
    public ResultResponse(int code,String msg){
        this.code = code;
        this.msg = msg;
    }
    //失败了 不携带数据、不携带信息的方法
    public static ResultResponse  fail(){
        return  new ResultResponse<>(CommonResultEnums.FAIL.getCode(),CommonResultEnums.FAIL.getMsg());
    }
    //失败了 不携带数据、携带信息的方法
    public static ResultResponse  fail(String msg){
        return  new ResultResponse<>(CommonResultEnums.FAIL.getCode(),msg);
    }
    //失败了 携带数据、携带信息的方法
    public static <T>ResultResponse  fail(String msg,T t){
        return  new ResultResponse<>(CommonResultEnums.FAIL.getCode(),msg,t);
    }
    //失败了 携带数据、不携带信息的方法
    public static <T>ResultResponse  fail(T t){
        return  new ResultResponse<>(CommonResultEnums.FAIL.getCode(),CommonResultEnums.FAIL.getMsg(),t);
    }
    //成功 携带数据
    public static <T> ResultResponse success(T t){
        return new ResultResponse(CommonResultEnums.SUCCESS.getCode(),CommonResultEnums.SUCCESS.getMsg(),t);
    }
    //成功 不携带数据
    public static <T> ResultResponse success(){
        return new ResultResponse(CommonResultEnums.SUCCESS.getCode(),CommonResultEnums.SUCCESS.getMsg());
    }
}
