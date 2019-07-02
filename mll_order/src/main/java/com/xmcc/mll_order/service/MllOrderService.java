package com.xmcc.mll_order.service;

import com.xmcc.mll_common.orderDto.MllOrderDTO;
import com.xmcc.mll_common.result.ResultResponse;
import com.xmcc.mll_order.dto.MllOrderItemDTO;

import java.util.List;

public interface MllOrderService {

    ResultResponse<MllOrderDTO> createOrder(List<MllOrderItemDTO> mllOrderItemDTOList);

}
