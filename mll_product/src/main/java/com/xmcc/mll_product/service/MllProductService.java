package com.xmcc.mll_product.service;

import com.xmcc.mll_common.orderDto.MllOrderDTO;
import com.xmcc.mll_common.result.ResultResponse;
import com.xmcc.mll_product.entity.MllProduct;

public interface MllProductService {

    //修改库存
    ResultResponse<MllOrderDTO> updateStock(MllOrderDTO mllOrderDTO);

    //根据id查询商品
    ResultResponse<MllProduct> queryById(Long id);

    //回滚商品库存扣减
    void stockRollback(MllOrderDTO mllOrderDTO);

}
