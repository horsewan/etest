package com.eningqu.pay.common.tools;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;

/**
 * @desc TODO  微信接口请求 请求需要双向证书
 * @author Yanghuangping
 * @version 1.0
 * @since 2018/8/3 16:33
 **/

public class WXCertHttpTools {

    private static int socketTimeout = 10000;// 连接超时时间，默认10秒
    private static int connectTimeout = 30000;// 传输超时时间，默认30秒
    private static RequestConfig requestConfig;// 请求器的配置
    private static CloseableHttpClient httpClient;// HTTP请求器

    /**
     * 通过Https往API post xml数据
     *
     * @param url      API地址
     * @param xmlObj   要提交的XML数据对象
     * @param mchId    商户ID
     * @param certPath 证书位置
     * @return
     */
    public static String post(String url, String xmlObj, String mchId, String certPath) throws Exception {
        String result = null;
        HttpPost httpPost = null;
        try {
            // 加载证书
            loadCert(mchId, certPath);
            httpPost = new HttpPost(url);
            // 得指明使用UTF-8编码，否则到API服务器XML的中文不能被成功识别
            StringEntity postEntity = new StringEntity(xmlObj, "UTF-8");
            httpPost.addHeader("Content-Type", "text/xml");
            httpPost.setEntity(postEntity);
            // 根据默认超时限制初始化requestConfig
            requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeout).setConnectTimeout(connectTimeout).build();
            // 设置请求器的配置
            httpPost.setConfig(requestConfig);

            HttpResponse response = null;
            try {
                response = httpClient.execute(httpPost);
            } catch (IOException e) {
                e.printStackTrace();
            }
            HttpEntity entity = response.getEntity();
            try {
                result = EntityUtils.toString(entity, "UTF-8");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } finally {
            if(httpPost != null){
                httpPost.abort();
            }
        }
        return result;
    }

    /**
     * 加载证书
     *
     * @param mchId    商户ID
     * @param certPath 证书位置
     * @throws Exception
     */
    private static void loadCert(String mchId, String certPath) throws Exception {
        // 证书密码，默认为商户ID
        String key = mchId;
        // 证书的路径
        String path = certPath;
        // 指定读取证书格式为PKCS12
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        // 读取本机存放的PKCS12证书文件
//        FileInputStream instream = new FileInputStream(new File(path));
        InputStream certStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("cert/apiclient_cert.p12");
        try {
            // 指定PKCS12的密码(商户ID)
            keyStore.load(certStream, key.toCharArray());
        } finally {
            certStream.close();
        }

        // 加载证书
        SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, key.toCharArray()).build();
        SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslcontext,
                new String[]{"TLSv1", "TLSv1.1", "TLSv1.2"}, null, SSLConnectionSocketFactory.getDefaultHostnameVerifier());

        httpClient = HttpClients.custom().setSSLSocketFactory(socketFactory).build();
    }
}
