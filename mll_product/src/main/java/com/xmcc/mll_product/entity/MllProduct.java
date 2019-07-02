package com.xmcc.mll_product.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MllProduct implements Serializable{
    private Long id;

    private String title;

    private String sellPoint;

    private BigDecimal price;

    private Integer stockCount;

    private String barcode;

    private String image;

    private String small_pic;

    private String status;

    private Date createTime;

    private Date updateTime;

    private String category;

    private String brand;

    private String spec;

}

