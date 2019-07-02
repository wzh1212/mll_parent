package com.xmcc.mll_common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class MllOrderItem implements Serializable {
    private Long id;

    private Long itemId;

    private Long goodsId;

    private Long orderId;

    private String title;

    private BigDecimal price;

    private Integer num;

    private BigDecimal totalFee;

    private String picPath;

    private String sellerId;

}
