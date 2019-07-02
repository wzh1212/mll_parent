package com.xmcc.mll_miaosha.service.impl;

import com.xmcc.mll_common.result.ResultResponse;
import com.xmcc.mll_common.util.SnowFlakeUtils;
import com.xmcc.mll_miaosha.common.MiaoshaOrderEnums;
import com.xmcc.mll_miaosha.entity.MllMiaoshaOrder;
import com.xmcc.mll_miaosha.entity.MllMiaoshaProduct;
import com.xmcc.mll_miaosha.mapper.MiaoshaOrderMapper;
import com.xmcc.mll_miaosha.mapper.MiaoshaProductMapper;
import com.xmcc.mll_miaosha.service.MiaoshaService;
import com.xmcc.mll_miaosha.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Random;

@Service
@Slf4j
public class MiaoshaServiceImpl implements MiaoshaService {

    @Autowired
    private MiaoshaProductMapper miaoshaProductMapper;

    @Autowired
    private MiaoshaOrderMapper miaoshaOrderMapper;

    @Autowired
    private SnowFlakeUtils snowFlakeUtils;

    @Autowired
    private RedisUtils redisUtils;


    // 根据 商品 id 查询商品
    @Override
    public ResultResponse<MllMiaoshaProduct> queryById(long productId) {
        return ResultResponse.success(miaoshaProductMapper.queryById(productId));
    }

    // 在类加载的时候初始化一些数据，也就是项目中常说的：热数据预加载
    // 在这里可以先把 秒杀商品的库存进行加载到 redis
    @PostConstruct
    public void initRedisStock(){
        log.info("初始化执行的方法");
        List<MllMiaoshaProduct> mllMiaoshaProducts = miaoshaProductMapper.queryAll();
        if (mllMiaoshaProducts == null){
            return;
        }
        // 商品id + 固定的前缀 为 key，商品的库存为 value
        //
        for (MllMiaoshaProduct mllMiaoshaProduct : mllMiaoshaProducts){
            redisUtils.set("stock_" + mllMiaoshaProduct.getProductId(),mllMiaoshaProduct.getStockCount());
            redisUtils.set("productId_" + mllMiaoshaProduct.getProductId(),mllMiaoshaProduct);
        }
    }


    // 根据 商品id 、用户id 进行秒杀商品
    @Override
    @Transactional
    public ResultResponse doMiaosha(long productId, String userId) {
        // 先通过 redis 进行库存判断，减少数据库的访问次数，自减，如果库存小于 0，那么就直接返回
        long decr = redisUtils.incrBy("stock_" + productId,-1);
        if (decr < 0){
            return ResultResponse.fail(MiaoshaOrderEnums.NOT_ENOUGH.getMsg());
        }


        //1.判断库存是否足够
        MllMiaoshaProduct mllMiaoshaProduct = (MllMiaoshaProduct) redisUtils.get("productId_" + productId);
//        MllMiaoshaProduct mllMiaoshaProduct = miaoshaProductMapper.queryById(productId);
//        int stockCount = mllMiaoshaProduct.getStockCount();
//        if (stockCount <= 0){
//            return ResultResponse.fail(MiaoshaOrderEnums.NOT_ENOUGH.getMsg());
//        }

        //2.判断用户是否已经秒杀过
//        int result = miaoshaOrderMapper.queryByUserIdAndProductId(productId, userId);  // 会导致用户多次下订单
//        if (result > 0){
//            return ResultResponse.fail(MiaoshaOrderEnums.FAIL.getMsg());
//        }

        //在写入订单之前通过 setnx 来保证原子性，key为商品id + 用户id，value随便，这个只是判断是否具有秒杀资格，而不是判断是否秒杀成功
        boolean setnx = redisUtils.setnx(productId + "_" + userId, 1);
        if (!setnx){
            // 已经秒杀直接返回，清除redis记录，放在订单完成，或者订单取消的业务，或者秒杀结束
            return ResultResponse.fail(MiaoshaOrderEnums.FAIL.getMsg());
        }

        //3.减少秒杀商品库存
        int i = miaoshaProductMapper.updateStockById(productId);
        if (i == 0){ // 解决订单秒杀时的重复问题
            // 如果没有修改成功，直接返回，也是 MySQL 行级锁的作用
            return ResultResponse.fail(MiaoshaOrderEnums.NOT_ENOUGH.getMsg());
        }
        // 4、写入订单
        MllMiaoshaOrder mllMiaoshaOrder = new MllMiaoshaOrder();
        // 订单 id
        long orderId = snowFlakeUtils.nextId();
        mllMiaoshaOrder.setOrderId(orderId);
        // 价格
        mllMiaoshaOrder.setPayment(mllMiaoshaProduct.getMiaoshaPrice());
        mllMiaoshaOrder.setProductId(productId);
        mllMiaoshaOrder.setUserId(userId);
        mllMiaoshaOrder.setStatus(1);
        miaoshaOrderMapper.insert(mllMiaoshaOrder);
        //这里忽略了很多信息，如果是完整项目，需要将收货信息，以及支付信息都返回，方便对接物流系统、支付系统等
        return ResultResponse.success(mllMiaoshaOrder);
    }

    // 验证码
    @Override
    public BufferedImage createVerifyCode(long productId) {
        //验证码图片 宽度 高度
        int width = 80;
        int height = 32;
        //create the image
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        //获得画笔
        Graphics g = image.getGraphics();
        // 背景颜色等设置
        g.setColor(new Color(0xDCDCDC));
        g.fillRect(0, 0, width, height);
        // 边框设置
        g.setColor(Color.black);
        g.drawRect(0, 0, width - 1, height - 1);
        //生成的数字 在图片中的坐标
        Random rdm = new Random();
        // make some confusion
        for (int i = 0; i < 50; i++) {
            int x = rdm.nextInt(width);
            int y = rdm.nextInt(height);
            g.drawOval(x, y, 0, 0);
        }
        // 生成验证码图片的内容  这里是一个数学表达式 例如 2+3-5 然后写入验证码图片中
        String verifyCode = generateVerifyCode(rdm);
        g.setColor(new Color(0, 100, 0));
        g.setFont(new Font("Candara", Font.BOLD, 24));
        g.drawString(verifyCode, 8, 24);
        g.dispose();
        //计算出验证码中数学表达式的结果 并放入redis中,并设置60秒过期
        int result = calc(verifyCode);
        redisUtils.setex(productId+"_code",result,60);
        //输出图片
        return image;
    }
    private static char[] ops = new char[] {'+', '-', '*'};
    /**
     * + - *
     * */
    private String generateVerifyCode(Random rdm) {
        int num1 = rdm.nextInt(10);
        int num2 = rdm.nextInt(10);
        int num3 = rdm.nextInt(10);
        char op1 = ops[rdm.nextInt(3)];
        char op2 = ops[rdm.nextInt(3)];
        String exp = ""+ num1 + op1 + num2 + op2 + num3;
        return exp;
    }
    private static int calc(String exp) {
        try {
            //通过ScriptEngine引擎计算出结果  这个不怎么常用了
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("JavaScript");
            return (Integer)engine.eval(exp);
        }catch(Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


}
