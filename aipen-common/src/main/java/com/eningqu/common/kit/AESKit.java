package com.eningqu.common.kit;

import cn.hutool.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Security;

/**
 *
 * @desc TODO  
 * @author     Yanghuangping
 * @since      2018/8/6 16:00
 * @version    1.0
 *
 **/

public class AESKit {

    public static final String ALGORITHM_MODE_PADDING = "AES/ECB/PKCS7Padding";
    public static final String ALGORITHM = "AES";


    private static Logger logger = LoggerFactory.getLogger(AESKit.class);

    public static byte[] encrypt (byte[] datas, byte[] key){
        try {
            Security.addProvider(CommonKit.PROVIDER);
            Cipher cipher = Cipher.getInstance(ALGORITHM_MODE_PADDING, "BC");
            SecretKeySpec keySpec = new SecretKeySpec(key, ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            return cipher.doFinal(datas);
        } catch (Exception e) {
            logger.error("AES/ECB/PKCS7Padding 加密异常，{}", e.getMessage());
        }
        return null;
    }

    public static String decrypt(byte[] datas, byte[] key){
        try {
            Security.addProvider(CommonKit.PROVIDER);
            Cipher cipher = Cipher.getInstance(ALGORITHM_MODE_PADDING, "BC");
            SecretKeySpec keySpec = new SecretKeySpec(key, ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] decode = cipher.doFinal(datas);
            return StrUtil.str(decode, "UTF-8");
        } catch (Exception e) {
            logger.error("AES/ECB/PKCS7Padding 解密异常，{}", e.getMessage());
        }
        return null;
    }

}
