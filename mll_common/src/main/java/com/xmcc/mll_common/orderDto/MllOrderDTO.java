package com.xmcc.mll_common.orderDto;

import com.xmcc.mll_common.orderDto.MllOrderItemDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("订单 DTO")
public class MllOrderDTO implements Serializable {

    //订单id
    @ApiModelProperty(value = "商品 id",dataType = "long")
    @NotNull(message = "商品id不能为null")
    private Long orderId;

    //订单总价格 不考虑邮费
    private BigDecimal payment;

    //订单项
    @ApiModelProperty(value = "订单项集合",dataType = "List")
    @Valid   //嵌套参数验证
    private List<MllOrderItemDto> mllOrderItemList;

}
