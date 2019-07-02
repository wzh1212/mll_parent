package com.xmcc.mll_miaosha.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MllMiaoshaOrder implements Serializable {
    private long orderId;
    private BigDecimal payment;
    private long productId;
    private String userId;
    private int status;
}

