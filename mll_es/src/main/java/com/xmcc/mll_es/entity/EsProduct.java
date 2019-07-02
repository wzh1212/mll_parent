package com.xmcc.mll_es.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EsProduct implements Serializable{
    private long productId;
    private String title;
    private String sell_point;
    private double price;
    private Date create_time;
    private String category;
    private String brand;
}
