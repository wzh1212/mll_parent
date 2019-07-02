package com.xmcc.mll_miaosha.mapper;

import com.xmcc.mll_miaosha.entity.MllMiaoshaOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MiaoshaOrderMapper {

    int queryByUserIdAndProductId(@Param("productId") long productId, @Param("userId") String userId);

    int insert(MllMiaoshaOrder mllMiaoshaOrder);

}
