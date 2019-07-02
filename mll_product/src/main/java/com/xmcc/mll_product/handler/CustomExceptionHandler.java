package com.xmcc.mll_product.handler;

import com.xmcc.mll_common.exception.CustomException;
import com.xmcc.mll_common.result.ResultEnums;
import com.xmcc.mll_common.result.ResultResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 全局异常处理
 */
//AOP 拦截springboot controller层
@ControllerAdvice
@RestController  //前后端分离都是返回的json数据
@Slf4j
public class CustomExceptionHandler {


    @ExceptionHandler(Exception.class)
    public ResultResponse<ResultEnums> exceptionHandler(HttpServletRequest request, HttpServletResponse response,Exception exception){
        log.error("request请求为:{},异常为：{}",request.getRequestURL(),exception);
        // 如果是自定义异常
        if (exception instanceof CustomException){
            CustomException customException = (CustomException)exception;
            return ResultResponse.fail(customException.getMessage());
        }
        return ResultResponse.fail(exception.getCause().getMessage());
    }

}
