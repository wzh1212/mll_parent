package com.xmcc.mll_product.mapper;


import com.xmcc.mll_product.entity.MllProduct;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MllProductMapper {

    //根据商品id修改数量
    int updateStock(@Param("productId") Long productId, @Param("num") Integer num);

    // 根据 id 查询商品
    MllProduct queryById(@Param("productId") Long productId);

    //回滚商品库存扣减
    int stockRollback(@Param("productId") Long productId,@Param("num") Integer num);

}
