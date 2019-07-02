package com.xmcc.mll_order.client;

import com.xmcc.mll_common.api.ProductOrderApi;
import com.xmcc.mll_common.orderDto.MllOrderDTO;
import com.xmcc.mll_common.result.ResultResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "PRODUCT")
public interface ProductApi extends ProductOrderApi {

    @PostMapping("product/updateStock")
    ResultResponse<MllOrderDTO> updateStock(MllOrderDTO mllOrderDTO);
}
