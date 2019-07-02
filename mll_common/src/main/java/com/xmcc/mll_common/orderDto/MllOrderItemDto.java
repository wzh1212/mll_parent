package com.xmcc.mll_common.orderDto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ApiModel("订单项实体类")
public class MllOrderItemDto implements Serializable{

    private Long id;

    private Long itemId;

    @ApiModelProperty(value = "商品id",dataType = "long")
    @NotNull(message = "商品id不能为空")
    private Long goodsId;

    private Long orderId;

    private String title;

    private BigDecimal price;

    @NotNull(message = "商品数量不能为空")
    @Min(value = 1,message = "数量不能少于一件")
    @ApiModelProperty(value = "商品数量",dataType = "Integer")
    private Integer num;

    private BigDecimal totalFee;

    private String picPath;

    private String sellerId;

}
