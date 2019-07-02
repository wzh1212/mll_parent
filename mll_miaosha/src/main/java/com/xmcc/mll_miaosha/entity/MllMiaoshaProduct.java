package com.xmcc.mll_miaosha.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * 秒杀商品
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MllMiaoshaProduct implements Serializable {
    private long id;
    private long productId;
    private String title;
    private BigDecimal price;
    private BigDecimal miaoshaPrice;
    private int stockCount;
    private String image;
    private Date startDate;
    private Date endDate;
    private String brand;
    private String spec;


}
