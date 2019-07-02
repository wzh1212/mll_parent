package com.xmcc.mll_miaosha.service;

import com.xmcc.mll_common.result.ResultResponse;
import com.xmcc.mll_miaosha.entity.MllMiaoshaProduct;

import java.awt.image.BufferedImage;

public interface MiaoshaService {

    // 根据 商品 id 查询商品
    ResultResponse<MllMiaoshaProduct> queryById(long productId);

    // 根据 商品id 、用户id 进行秒杀商品
    ResultResponse doMiaosha(long productId, String userId);

    // 验证码
    BufferedImage  createVerifyCode(long productId);

}
