package com.eningqu.pay.common.tools;

import cn.hutool.core.util.URLUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayConstants;
import com.alipay.api.internal.util.AlipaySignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

/**
 *
 * @desc TODO  支付宝工具类
 * @author     Yanghuangping
 * @since      2018/7/30 19:44
 * @version    1.0
 *
 **/

public class AliTools {

    public static final Logger logger = LoggerFactory.getLogger(AliTools.class);
    public static final String DEFAULT_CHARSET = "UTF-8";

    /**
     * TODO 字符串拼接 对value进行URLEncoder
     * @param paramsMap
     * @return
     */
    public static String mapToStr(SortedMap<String, String> paramsMap, boolean isURLEncode){
        StringBuilder sb = new StringBuilder();
        Set es = paramsMap.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            if (null != v && !"".equals(v) && !"sign".equalsIgnoreCase(k)) {
                sb.append(k)
                  .append("=")
                  .append(isURLEncode ? URLUtil.encode(v, AlipayConstants.CHARSET_UTF8) : v)
                  .append("&");
            }
        }
        return sb.toString().substring(0, sb.lastIndexOf("&"));
    }

    public static String SHA256WithRSA(String content, String privateKey, String signType){
        try {
            return AlipaySignature.rsaSign(content, privateKey, AlipayConstants.CHARSET_UTF8, signType);
        } catch (AlipayApiException e) {
            logger.error("SHA256WithRSA 签名失败，{}",e);
        }
        return null;
    }
}
