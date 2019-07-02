package com.xmcc.mll_order.service.impl;

import com.google.common.collect.Lists;
import com.xmcc.mll_common.api.ProductOrderApi;
import com.xmcc.mll_common.exception.CustomException;
import com.xmcc.mll_common.orderDto.MllOrderDTO;
import com.xmcc.mll_common.orderDto.MllOrderItemDto;
import com.xmcc.mll_common.result.CommonResultEnums;
import com.xmcc.mll_common.result.ResultResponse;
import com.xmcc.mll_common.util.BigDecimalUtil;
import com.xmcc.mll_common.util.SnowFlakeUtils;
import com.xmcc.mll_order.common.OrderEnums;
import com.xmcc.mll_order.dto.MllOrderItemDTO;
import com.xmcc.mll_order.mapper.MllOrderItemMapper;
import com.xmcc.mll_order.mapper.MllOrderMapper;
import com.xmcc.mll_order.entity.MllOrder;
import com.xmcc.mll_order.rabbitmq.RabbitSenderUtil;
import com.xmcc.mll_order.service.MllOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;


@Service
@Slf4j
public class MllOrderServiceImpl implements MllOrderService {


    @Autowired
    private MllOrderMapper mllOrderMapper;

    @Autowired
    private MllOrderItemMapper mllOrderItemMapper;

    @Autowired
    private ProductOrderApi productOrderApi;

    @Autowired
    private SnowFlakeUtils orderSnowFlake;

    @Autowired
    private RabbitSenderUtil rabbitSenderUtil;


    /**
     * 这儿没有其它业务 直接写在一起了 不去单独封装了 代码较多 但是逻辑简单
     * @param mllOrderItemDTOList
     * @return
     */
    @Override
    @Transactional
    public ResultResponse<MllOrderDTO> createOrder(List<MllOrderItemDTO> mllOrderItemDTOList) {
        //参数都已经通过 @Valid 校验了 这儿不用校验了 如果测试人员测出问题 再添加判断就是 比较简单

        //组装商品微服务需要的参数MllOrderDTO
        MllOrderDTO mllOrderDTO = new MllOrderDTO();
        //通过snowflake生成id  这个id非常重要 可以作为以后扩展的依据
        // 如果还是没有看snowflake的同学一定去看懂
        long orderId = orderSnowFlake.nextId();
        mllOrderDTO.setOrderId(orderId);
        //创建订单项
        List<MllOrderItemDto> mllOrderItemList = Lists.newArrayList();
        for (MllOrderItemDTO mllOrderItemDTO : mllOrderItemDTOList) {
            MllOrderItemDto mllOrderItem = new MllOrderItemDto();
            // 设置 orderId
            mllOrderItem.setOrderId(orderId);
            // 商品 id 与数理
            mllOrderItem.setGoodsId(mllOrderItemDTO.getProductId());
            mllOrderItem.setNum(mllOrderItemDTO.getQuantity());
            mllOrderItemList.add(mllOrderItem);
        }
        // 组装参数
        mllOrderDTO.setMllOrderItemList(mllOrderItemList);
        //调用商品微服务接口
        ResultResponse<MllOrderDTO> mllOrderDTOResultResponse = productOrderApi.updateStock(mllOrderDTO);
        //判断是否是失败  如果失败获取商品微服务那边的失败信息即可 事务回滚
        if (mllOrderDTOResultResponse.getCode() == CommonResultEnums.FAIL.getCode()){
            log.error("订单创建失败，异常原因为：{}",mllOrderDTOResultResponse.getMsg());
            throw  new CustomException(mllOrderDTOResultResponse.getMsg());
        }
        MllOrderDTO data = mllOrderDTOResultResponse.getData();
        insertOrderByOrderDTO(data);
        return ResultResponse.success(data);
    }

    /**
     * 根据 dto 插入 order 表
     * @param data
     */
    private void insertOrderByOrderDTO(MllOrderDTO data){
//        MllOrder mllOrder = new MllOrder();
//        mllOrder.setOrderId(data.getOrderId());
//        //设置未未支付状态
//        mllOrder.setStatus(String.valueOf(OrderEnums.NO_PAY.getCode()));
//        //循环插入订单项并计算总价格
//        BigDecimal totalPrice = new BigDecimal("0");
//        for (MllOrderItemDto mllOrderItemDto : data.getMllOrderItemList()){
//            totalPrice = BigDecimalUtil.add(totalPrice,mllOrderItemDto.getTotalFee());
//            // 插入订单项
//            insertOrderItem(mllOrderItemDto);
//        }
//        //设置总价格
//        mllOrder.setPayment(totalPrice);
//        //订单还有很多其它信息 这儿用不到 暂时不去关注 重要一定的就是根据当前登录用户 获得用户的信息 收获地址等
//        mllOrderMapper.insertOrder(mllOrder);

        try {
            MllOrder mllOrder = new MllOrder();
            mllOrder.setOrderId(data.getOrderId());
            //这儿数据库设计的时候 把状态用的varchar类型 请把数据库status字段varchar长度改为3
            //设置未支付状态
            mllOrder.setStatus(String.valueOf(OrderEnums.NO_PAY.getCode()));
            //循环插入订单项并计算总价格
            BigDecimal totalPrice = new BigDecimal("0");
            for (MllOrderItemDto mllOrderItemDto : data.getMllOrderItemList()) {
                totalPrice = BigDecimalUtil.add(totalPrice, mllOrderItemDto.getTotalFee());
                //插入订单项
                insertOrderItem(mllOrderItemDto);
            }
            //设置总价格
            mllOrder.setPayment(totalPrice);
            //订单还有很多其它信息 这儿用不到 暂时不去关注 重要一定的就是根据当前登录用户 获得用户的信息 收获地址等
            mllOrderMapper.insertOrder(mllOrder);
            //模拟异常
            throw  new RuntimeException("出现异常了哦");
        }catch (Exception e){
            log.error("扣减库存后，插入订单与订单项到数据库出现异常，异常信息为:{}",e);
            //发送错误消息通过rabbitmq 消息驱动 来实现最终一致性
            rabbitSenderUtil.orderErrorToProduct(orderSnowFlake.nextId(),data);
            //抛出异常让事务回滚
            throw  new RuntimeException(e);
        }

    }

    /**
     * 插入订单项
     */
    private void insertOrderItem(MllOrderItemDto mllOrderItemDto){
        //这个是商品分得更细的商品详情表 这儿直接使用商品id即
        mllOrderItemDto.setItemId(mllOrderItemDto.getGoodsId());
        //该有的字段都有了
        mllOrderItemMapper.insertOrder(mllOrderItemDto);
    }



}
