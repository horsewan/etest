package com.eningqu.pay.common.union.config;


/**
 * <一句话功能简述>
 * <功能详细描述>配置信息
 * 
 * @author  Administrator
 * @version  [版本号, 2014-8-29]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class SwiftpassConfig {
    
    /**
     * 交易密钥
     */
    public static String key="3fd2678956f7ea756109a6a479019c60";
    
    /**
     * 商户号
     */
    public static String mch_id="QRA584273722KLD";




    public static String unionService="unified.trade.native";

    public static String unionVersion="1.0";

    public static String unionCharset="UTF-8";

    public static String unionSignType="MD5";

    public static String mchCreateIp="120.78.169.242";

    /**
     * 请求url
     */
    public static String req_url="https://qra.95516.com/pay/gateway";
    
    /**
     * 通知url
     */
    public static String notify_url="http://120.78.169.242:9090/api/union/notify";
    
    /*static{
        Properties prop = new Properties();   
        InputStream in = SwiftpassConfig.class.getResourceAsStream("/config.properties");   
        try {   
            prop.load(in);   
//            key = prop.getProperty("key").trim();
//            mch_id = prop.getProperty("mch_id").trim();
            req_url = prop.getProperty("req_url").trim();   
            notify_url = prop.getProperty("notify_url").trim();
            unionService = prop.getProperty("union_service").trim();
            unionVersion = prop.getProperty("union_version").trim();
            unionCharset = prop.getProperty("union_charset").trim();
            unionSignType = prop.getProperty("union_sign_type").trim();
            mchCreateIp = prop.getProperty("mch_create_ip").trim();
        } catch (IOException e) {
            e.printStackTrace();   
        } 
    }*/

}
