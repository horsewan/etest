package com.eningqu.common.kit;

import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 *
 * @desc TODO  七牛云上传
 * @author     Yanghuangping
 * @since      2018/5/14 14:00
 * @version    1.0
 *
 **/

public class QiNiuKit {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private Auth auth;

    private String domain;
    private String bucket;

    private Zone zone;
    private Configuration configuration;
    private UploadManager uploadManager;

    public QiNiuKit(Auth auth, String domain, String bucket) {
        this.auth = auth;
        this.domain = domain;
        this.bucket = bucket;
        zone = Zone.autoZone();
        configuration = new Configuration(zone);
        uploadManager = new UploadManager(configuration);
    }

    public String getUploadToken(){
        return auth.uploadToken(bucket);
    }

    /**
     * TODO 文件上传
     * @param file 文件
     * @param key  文件名  null值则取文件的hash值作为文件名
     * @return
     */
    public String upload(File file, String key) throws QiniuException{
        Response response = uploadManager.put(file, key, getUploadToken());
        return response.bodyString();
    }

    /**
     * TODO 文件上传
     * @param inputStream 输入流
     * @param fileName         文件名  null值则取文件的hash值作为文件名
     * @return
     */
    public String upload(InputStream inputStream, String fileName) throws QiniuException {
        Response response = uploadManager.put(inputStream, fileName, getUploadToken(), null, null);
        return response.bodyString();
    }


    /**
     * TODO 获取下载地址
     * @param fileName
     * @return
     * @throws UnsupportedEncodingException
     */
    public String downloadUrl(String fileName) throws UnsupportedEncodingException {
        String encodedFileName = URLEncoder.encode(fileName, "utf-8");
        String publicUrl = String.format("%s/%s", domain, encodedFileName);
        // 1小时，可以自定义链接过期时间
        long expireInSeconds = 3600;
        String finalUrl = auth.privateDownloadUrl(publicUrl, expireInSeconds);
        return finalUrl;
    }

    public String downloadPathUrl(String fileName) throws UnsupportedEncodingException {
        //设置好账号的ACCESS_KEY和SECRET_KEY
        String ACCESS_KEY = "fP6KfIAAIJG0ZpEQQGxwbfu6TZyF_r2mCt5GHRUb";
        String SECRET_KEY = "V4cKW3F5X0BxyGFua_A606uuQB2asJUHIJTt03TX";
        //密钥配置
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);//products-image
        //构造私有空间的需要生成的下载的链接
        String encodedFileName = URLEncoder.encode(fileName, "utf-8");
        String publicUrl = String.format("%s/%s", "http://download.futruedao.com", encodedFileName);
        //调用privateDownloadUrl方法生成下载链接,第二个参数可以设置Token的过期时间
       return auth.privateDownloadUrl(publicUrl, 3600);

    }
}
