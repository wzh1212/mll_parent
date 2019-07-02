package com.xmcc.mll_product.service.impl;

import com.xmcc.mll_common.entity.MllOrderItem;
import com.xmcc.mll_common.exception.CustomException;
import com.xmcc.mll_common.orderDto.MllOrderDTO;
import com.xmcc.mll_common.orderDto.MllOrderItemDto;
import com.xmcc.mll_common.result.ProductEnums;
import com.xmcc.mll_common.result.ResultResponse;
import com.xmcc.mll_common.util.BigDecimalUtil;
import com.xmcc.mll_common.util.SnowFlakeUtils;
import com.xmcc.mll_product.entity.MllProduct;
import com.xmcc.mll_product.mapper.MllProductMapper;
import com.xmcc.mll_product.service.MllProductService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.List;


@Service
@Slf4j
public class MllProductServiceImpl implements MllProductService {

    @Autowired
    private MllProductMapper mllProductMapper;

    @Autowired
    private SnowFlakeUtils productSnowFlake;

    /**
     * 一个订单中 商品不会有太多 循环修改问题也不大
     * 高并发情况下不能在代码中判断库存，通过mysql的行级锁判断
     * @param mllOrderDTO
     * @return
     */

    //修改库存
    @Override
    @Transactional
    public ResultResponse<MllOrderDTO> updateStock(MllOrderDTO mllOrderDTO) {
        //获得订单的商品列表
        List<MllOrderItemDto> mllOrderItemList = mllOrderDTO.getMllOrderItemList();
        //循环根据id修改库存
        for (MllOrderItemDto mllOrderItemDto : mllOrderItemList) {
            //根据id查询商品
            ResultResponse<MllProduct> mllProductResultResponse = queryById(mllOrderItemDto.getGoodsId());
            // 获得根据 id 查询到的商品结果
            MllProduct mllProduct = mllProductResultResponse.getData();
            // 判断商品是否存在，不存在--> 直接拋异常进行事务回滚
            if (mllProduct == null){
                log.error("商品库存扣减中出现商品id对应的商品不存在，商品id：{}",mllOrderItemDto.getGoodsId());
                throw new CustomException(ProductEnums.PRODUCT_NOT_EXISTS.getMsg());
            }
            int i = mllProductMapper.updateStock(mllOrderItemDto.getGoodsId(), mllOrderItemDto.getNum());
            //说明修改不成功 库存不足  抛出异常事务回滚
            if (i < 1){
                log.error("商品库存扣减中出现库存不足，商品id：{}",mllOrderItemDto.getGoodsId());
                throw new CustomException(ProductEnums.STOCK_NOT_ENOUGH.getMsg());
            }

            //修改成功就将信息设置进入MllOrderDTO 返回给订单微服务去存储
            //设置id
            mllOrderItemDto.setId(productSnowFlake.nextId());
            //价格
            mllOrderItemDto.setPrice(mllProduct.getPrice());
            //总价格
            mllOrderItemDto.setTotalFee(BigDecimalUtil.multi(mllProduct.getPrice(),mllOrderItemDto.getNum()));
            //标题
            mllOrderItemDto.setTitle(mllProduct.getTitle());
            //图片
            mllOrderItemDto.setPicPath(mllProduct.getImage());
        }

        //传回去
        return ResultResponse.success(mllOrderDTO);
    }

    //根据id查询商品
    @Override
    public ResultResponse<MllProduct> queryById(Long id) {
        MllProduct mllProduct = mllProductMapper.queryById(id);
        if (mllProduct == null){
            return ResultResponse.fail(ProductEnums.PRODUCT_NOT_EXISTS.getMsg());
        }
        return ResultResponse.success(mllProduct);
    }

    // 回滚商品库存扣减
    @Override
    @Transactional
    public void stockRollback(MllOrderDTO mllOrderDTO) {
        List<MllOrderItemDto> mllOrderItemList = mllOrderDTO.getMllOrderItemList();
        for (MllOrderItemDto mllOrderItemDto : mllOrderItemList){
            mllProductMapper.stockRollback(mllOrderItemDto.getGoodsId(),mllOrderItemDto.getNum());
        }

    }
}
