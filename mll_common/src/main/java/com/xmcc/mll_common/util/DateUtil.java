package com.xmcc.mll_common.util;

import java.util.Date;

/**
 * 时间工具类
 */
public class DateUtil {

    public static Date expire(long expire){
        //当前时间 + 过期时间
        long l = System.currentTimeMillis() + expire;
        return new Date(l);
    }


}
