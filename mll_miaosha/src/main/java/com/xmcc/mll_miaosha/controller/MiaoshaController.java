package com.xmcc.mll_miaosha.controller;

import com.xmcc.mll_common.result.ResultResponse;
import com.xmcc.mll_miaosha.entity.MllMiaoshaProduct;
import com.xmcc.mll_miaosha.service.MiaoshaService;
import com.xmcc.mll_miaosha.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.Map;
import java.util.UUID;


@Controller
@RequestMapping("miaosha")
@Slf4j
public class MiaoshaController {

    @Autowired
    private MiaoshaService miaoshaService;

    @Autowired
    private RedisUtils redisUtils;

    @GetMapping("page/{productId}")
    public ModelAndView page(@PathVariable("productId") Long productId, Map map){
        log.error("miaosha，接口被访问");
        // todo 注意当前没有做登录，所以暂时模拟一个登录号
        String currentUser =  "admin";
        // 根据 商品 id 查询要秒杀的商品
        ResultResponse<MllMiaoshaProduct> mllMiaoshaProductResultResponse = miaoshaService.queryById(productId);
        // 获取 要秒杀商品的信息
        MllMiaoshaProduct mllMiaoshaProduct = mllMiaoshaProductResultResponse.getData();

        //计算秒杀的时间与状态供前台使用
        long startAt = mllMiaoshaProduct.getStartDate().getTime();
        long endAt = mllMiaoshaProduct.getEndDate().getTime();
        long now = System.currentTimeMillis();

        // 秒杀状态
        int miaoshaStatus = 0;
        // 秒杀倒计时
        int remainSeconds = 0;
        if (now < startAt){  //秒杀还没开始，倒计时
            miaoshaStatus = 0;
            remainSeconds = (int) ((startAt - now)/1000);
        }else if (now > endAt){ //秒杀已经结束
            miaoshaStatus = 2;
            remainSeconds = 0;
        }else {  //秒杀进行中
            miaoshaStatus = 1;
            remainSeconds = 0;
        }

        map.put("miaoshaStatus", miaoshaStatus);
        map.put("remainSeconds", remainSeconds);
        map.put("product",mllMiaoshaProduct);
        map.put("user",currentUser);
        return  new ModelAndView("freemarker/miaosha",map);

    }


    @PostMapping("do_miaosha/{productId}/{verifyCode}")
    @ResponseBody
    public ResultResponse doMiaosha(@PathVariable("productId") long productId,
                                    @PathVariable("verifyCode") long verifyCod){

        // todo
        log.info("do_miaosha 被访问");
        // 进行验证码判断，因为登录后面都会统一拦截
        Integer code = (Integer) redisUtils.get(productId + "_code");
        if (verifyCod!=code){
            return ResultResponse.fail("验证码输入错误，请重新输入");
        }

        //首先肯定时判断是否登录，这个功能在后面的登录模块再来添加,这儿先定义一个userId
        //String userId = "123456";
        String userId = UUID.randomUUID().toString().replace("-","");
        return miaoshaService.doMiaosha(productId,userId);
    }


    @RequestMapping(value="/verifyCode/{productId}", method=RequestMethod.GET)
    @ResponseBody
    public ResultResponse<String> getMiaoshaVerifyCod(HttpServletResponse response,
                                                      @PathVariable("productId")long productId) {
        //都等判断用户是否登录
        try {
            //获得验证码图片
            BufferedImage image  = miaoshaService.createVerifyCode( productId);
            OutputStream out = response.getOutputStream();
            //写入输入流
            ImageIO.write(image, "JPEG", out);
            out.flush();
            out.close();
            return null;
        }catch(Exception e) {
            e.printStackTrace();
            return ResultResponse.fail();
        }
    }



}
