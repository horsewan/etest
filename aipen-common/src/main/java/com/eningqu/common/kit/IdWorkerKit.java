package com.eningqu.common.kit;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.toolkit.IdWorker;

/**
 *
 * @desc TODO  
 * @author     Yanghuangping
 * @since      2018/7/12 17:24
 * @version    1.0
 *
 **/

public class IdWorkerKit {

    /**
     * TODO 雪花算法，生成唯一号
     * @return
     */
    public static String uniqueId(){
        String value = String.valueOf(IdWorker.getId());
        return value.substring(2);
    }

    /**
     * TODO
     * @param len
     * @return
     */
    public static String uniqueId(int len){
        String value = String.valueOf(IdWorker.getId());
        return value.substring(value.length() - len);
    }

    // 订单自增数
    private static int ORDER_INCR = 1;
    // 最大的订单数
    private static int MAX_COUNT = 9999;

    /**
     * TODO 生成唯一订单号
     * @return
     */
    public static String uniqueOrderNumber(){
        StringBuilder sb = new StringBuilder();
        sb.append(DateUtil.format(DateUtil.date(), "yyMMddHHmmssSSS")).append(getIncrNum());
        return sb.toString();
    }

    /**
     * TODO 获取一个自增数
     * @return
     */
    private synchronized static String getIncrNum(){
        if(ORDER_INCR > MAX_COUNT){
            ORDER_INCR = 1;
        }
        // 自动补0  总共4位
        String num = String.format("%04d", ORDER_INCR);
        ORDER_INCR ++;
        return num;
    }
}
