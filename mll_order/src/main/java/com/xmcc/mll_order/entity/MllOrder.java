package com.xmcc.mll_order.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MllOrder implements Serializable {
    private Long orderId;

    private BigDecimal payment;

    private String paymentType;

    private String postFee;

    private String status;

    private Date createTime;

    private Date updateTime;

    private Date paymentTime;

    private Date consignTime;

    private Date endTime;

    private Date closeTime;

    private String shippingName;

    private String shippingCode;

    private String userId;

    private String buyerMessage;

    private String buyerNick;

    private String buyerRate;

    private String receiverAreaName;

    private String receiverMobile;

    private String receiverZipCode;

    private String receiver;

    private Date expire;

    private String invoiceType;

    private String sourceType;

    private String sellerId;

}
