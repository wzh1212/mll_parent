package com.xmcc.mll_order.mapper;

import com.xmcc.mll_order.entity.MllOrder;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MllOrderMapper {
    int insertOrder(MllOrder mllOrder);
}
