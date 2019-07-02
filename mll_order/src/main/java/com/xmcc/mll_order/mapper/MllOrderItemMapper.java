package com.xmcc.mll_order.mapper;

import com.xmcc.mll_common.orderDto.MllOrderItemDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MllOrderItemMapper {

    // 插入订单项
    int insertOrder(MllOrderItemDto mllOrderItem);

}
