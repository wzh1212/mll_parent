package com.xmcc.mll_order.controller;

import com.xmcc.mll_common.result.ResultResponse;
import com.xmcc.mll_order.dto.MllOrderItemDTO;
import com.xmcc.mll_order.service.MllOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("order")
@Api(description = "商品信息接口")
public class MllOrderController {

    @Autowired
    private MllOrderService mllOrderService;

    @PostMapping("create")
    @ApiOperation(value = "创建订单接口", httpMethod = "POST", response =ResultResponse.class)
    public ResultResponse create(
            @Valid @ApiParam(name="订单项集合",value = "传入json格式",required = true)
            @RequestBody List<MllOrderItemDTO> mllOrderItemDTOList){
        //这儿还需要获得用户的信息 ，之后做完登录模块再来完善
        // TODO
        return mllOrderService.createOrder(mllOrderItemDTOList);
    }

    private ResultResponse createOrderFallback(List<MllOrderItemDTO> mllOrderItemDTOList){
        return ResultResponse.fail();
    }

}
