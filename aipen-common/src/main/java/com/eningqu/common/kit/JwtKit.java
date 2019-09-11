package com.eningqu.common.kit;

import com.eningqu.common.base.vo.JwtJson;
import io.jsonwebtoken.*;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

/**
 *
 * @desc TODO  JWT验证工具类
 * @author     Yanghuangping
 * @date       2018/4/28 10:09
 * @version    1.0
 *
 **/
public class JwtKit {

    /**token不存在*/
    public static final int NOT_EXSIT_TOKEN = 4000;
    /**token已过期*/
    public static final int TOKEN_EXPIRE = 4001;
    /**token验证错误*/
    public static final int TOEKN_ERROR = 4002;

    /**token 秘钥*/
    public static final String JWT_SECERT = "eningqu98!";
    /**有效时间（毫秒）*/
    public static final long JWT_TTL = 60 * 60 * 1000;

    /**
     * TODO 签发JWT
     * @Author  科帮网
     * @param id
     * @param subject 可以是JSON数据 尽可能少
     * @param ttlMillis
     * @return  String
     * @Date	2017年11月24日
     * 2017年11月24日  张志朋  首次创建
     *
     */
    public static String createToken(String id, String subject, long ttlMillis) {
        long nowMillis = System.currentTimeMillis();
        Date now = DateTimeKit.getDate(nowMillis);
        SecretKey secretKey = getSecertKey();
        JwtBuilder builder = Jwts.builder()
                .setId(id)
                .setSubject(subject)   // 主题
                .setIssuer("凝趣科技")  // 签发者
                .setIssuedAt(now)      // 签发时间
                .signWith(SignatureAlgorithm.HS256, secretKey);
        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date expDate = DateTimeKit.getDate(expMillis);
            builder.setExpiration(expDate); // 过期时间
        }
        return builder.compact();
    }

    /**
     * TODO 验证JWT信息
     * @param token
     * @return
     */
    public static JwtJson verifyToken(String token) {
        Claims claims = null;
        try {
            claims = parseJWT(token);
            return JwtJson.ok().setClaims(claims);
        } catch (ExpiredJwtException e) {
            return JwtJson.error().setCode(TOKEN_EXPIRE);
        } catch (SignatureException e) {
            return JwtJson.error().setCode(TOEKN_ERROR);
        } catch (Exception e) {
            return JwtJson.error().setCode(TOEKN_ERROR);
        }
    }

    /**
     * TOKEN 获取秘钥
     * @return
     */
    public static SecretKey getSecertKey() {
        byte[] encodedKey = Base64.decodeBase64(JWT_SECERT);
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        return key;
    }

    /**
     *
     * TODO 解析JWT字符串
     * @param token
     * @return
     * @throws Exception
     */
    public static Claims parseJWT(String token) throws Exception {
        SecretKey secretKey = getSecertKey();
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }
}
