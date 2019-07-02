package com.xmcc.mll_common.api;

import com.xmcc.mll_common.orderDto.MllOrderDTO;
import com.xmcc.mll_common.result.ResultResponse;

public interface ProductOrderApi {

    /**
     * 供商品与当地调用，统一接口使用
     */
    ResultResponse<MllOrderDTO> updateStock(MllOrderDTO mllOrderDTO);

}
