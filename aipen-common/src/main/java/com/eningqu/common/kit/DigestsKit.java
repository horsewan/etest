package com.eningqu.common.kit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;

/**
 *
 * @desc TODO  支持SHA-1/MD5消息摘要的工具类. 返回ByteSource，可进一步被编码为Hex, Base64或UrlSafeBase64
 * @author     Yanghuangping
 * @date       2018/4/20 11:36
 * @version    1.0
 *
 **/
public class DigestsKit {

    private final static Logger logger = LoggerFactory.getLogger(DigestsKit.class);

    private static final String SHA1 = "SHA-1";
    private static final String MD5 = "MD5";

    /**
     * TODO 对输入字节数组进行md5散列.
     */
    public static byte[] md5(byte[] input) {
        return digest(input, MD5, null, 1);
    }
    public static byte[] md5(byte[] input, int iterations) {
        return digest(input, MD5, null, iterations);
    }

    /**
     * TODO 对输入字节数组进行sha1散列.
     */
    public static byte[] sha1(byte[] input) {
        return digest(input, SHA1, null, 1);
    }

    public static byte[] sha1(byte[] input, byte[] salt) {
        return digest(input, SHA1, salt, 1);
    }

    public static byte[] sha1(byte[] input, byte[] salt, int iterations) {
        return digest(input, SHA1, salt, iterations);
    }

    /**
     * TODO 对字符串进行散列, 支持md5与sha1算法.
     */
    private static byte[] digest(byte[] input, String algorithm, byte[] salt, int iterations) {
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            if (salt != null) {
                digest.update(salt);
            }
            byte[] result = digest.digest(input);
            for (int i = 1; i < iterations; i++) {
                digest.reset();
                result = digest.digest(result);
            }
            return result;
        } catch (GeneralSecurityException e) {
            logger.error("", e);
        }
        return null;
    }

    /**
     * TODO 对文件进行md5散列.
     */
    public static byte[] md5(InputStream input){
        return digest(input, MD5);
    }

    public static byte[] md5(File file){
        return digest(file, MD5);
    }

    /**
     * TODO 对文件进行sha1散列.
     */
    public static byte[] sha1(InputStream input){
        return digest(input, SHA1);
    }

    public static byte[] sha1(File file){
        return digest(file, SHA1);
    }

    private static byte[] digest(InputStream input, String algorithm){
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            int bufferLength = 8 * 1024;
            byte[] buffer = new byte[bufferLength];
            int read = 0;
            read = input.read(buffer, 0, bufferLength);
            while (read > -1) {
                messageDigest.update(buffer, 0, read);
                read = input.read(buffer, 0, bufferLength);
            }
            return messageDigest.digest();
        } catch (IOException e) {
            logger.error("", e);
        } catch (GeneralSecurityException e) {
            logger.error("", e);
        }
        return null;
    }

    private static byte[] digest(File file, String algorithm){
        if(file == null){
            logger.error("file is null");
            return null;
        }
        if(!file.isFile()){
            logger.error("is not a file");
            return null;
        }
        BufferedInputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(file));
            return digest(in, algorithm);
        } catch (FileNotFoundException e) {
            logger.error("", e);
        } finally {
            if(in != null){
                try {
                    in.close();
                } catch (IOException e) {
                    logger.error("io close exception", e);
                }
            }
        }
        return null;
    }
}
