package com.eningqu.common.kit;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;

import org.bouncycastle.asn1.*;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @desc TODO  
 * @author     Yanghuangping
 * @since      2018/5/17 19:47
 * @version    1.0
 *
 **/

public class RSAKit {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private PublicKey publicKey;
    private PrivateKey privateKey;

    /*** 填充模式 RSA/ECB/NoPadding  RSA/ECB/PKCS1Padding */
    private static final String PADDING_MODE = "RSA/ECB/PKCS1Padding";

    /*** 密钥大小*/
    private int KEY_SIZE = 1024;
    /*** 字符编码*/
    private String CHARSET = "UTF-8";

    protected Lock lock;

    public RSAKit(String publicKeyBase64, String privateKeyBase64) {
        this.publicKey = generatePublicKey(publicKeyBase64);
        this.privateKey = generatePrivateKey(privateKeyBase64);
        lock = new ReentrantLock();
    }

    public String encrypt(String data) {

        int keyByteSize = KEY_SIZE / 8;
        int maxBlockSize = keyByteSize - 11;

        byte[] dataBytes = StrUtil.bytes(data, CHARSET);
        int dataLength = dataBytes.length;
        // 计算分段加密的block数 (向上取整)
        int nBlock = ( dataLength / maxBlockSize);
        if ((dataLength % maxBlockSize) != 0) {
            nBlock += 1;
        }

        ByteArrayOutputStream bos = null;

        try {

            this.lock.lock();

            Cipher cipher = Cipher.getInstance(PADDING_MODE);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            bos = new ByteArrayOutputStream(nBlock * keyByteSize);

            for (int offset = 0; offset < dataLength; offset += maxBlockSize) {
                int partLen = dataLength - offset;
                if (partLen > maxBlockSize) {
                    partLen = maxBlockSize;
                }
                // 得到分段加密结果
                byte[] cacheBlock = cipher.doFinal(dataBytes, offset, partLen);
                bos.write(cacheBlock);
            }
            bos.flush();
            return Base64.encode(bos.toByteArray());
        } catch (NoSuchAlgorithmException e) {
            logger.error("", e);
        } catch (NoSuchPaddingException e) {
            logger.error("", e);
        } catch (InvalidKeyException e) {
            logger.error("", e);
        } catch (IllegalBlockSizeException e) {
            logger.error("", e);
        } catch (BadPaddingException e) {
            logger.error("", e);
        } catch (IOException e) {
            logger.error("", e);
        } finally {
            if(bos != null){
                try {
                    bos.close();
                } catch (IOException e) {
                    logger.error("", e);
                }
            }
            this.lock.unlock();
        }
        return null;
    }


    public String decrypt(String dataBase64){

        byte[] dataBytes = Base64.decode(dataBase64);
        int dataLength = dataBytes.length;

        int keyByteSize = KEY_SIZE / 8;
        int maxBlockSize = keyByteSize - 11;
        // 计算分段加密的block数 (向上取整)
        int nBlock = (dataLength / keyByteSize);

        ByteArrayOutputStream bos = null;

        try {

            this.lock.lock();

            Cipher cipher = Cipher.getInstance(PADDING_MODE);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            bos = new ByteArrayOutputStream(nBlock * maxBlockSize);
            for (int offset = 0; offset < dataLength; offset += keyByteSize) {
                int inputLen = dataLength - offset;
                if (inputLen > keyByteSize) {
                    inputLen = keyByteSize;
                }
                /**得到分段解密密结果*/
                byte[] cacheBlock = cipher.doFinal(dataBytes, offset, inputLen);
                bos.write(cacheBlock);
            }
            bos.flush();
            return StrUtil.str(bos, CHARSET);
        } catch (NoSuchAlgorithmException e) {
            logger.error("", e);
        } catch (NoSuchPaddingException e) {
            logger.error("", e);
        } catch (InvalidKeyException e) {
            logger.error("", e);
        } catch (IllegalBlockSizeException e) {
            logger.error("", e);
        } catch (BadPaddingException e) {
            logger.error("", e);
        } catch (IOException e) {
            logger.error("", e);
        } finally {
            if(bos != null){
                try {
                    bos.close();
                } catch (IOException e) {
                    logger.error("", e);
                }
            }
            this.lock.unlock();
        }
        return null;
    }


    /**
     * 加载公钥
     *
     * @param publicKeyBase64
     * @return
     * @throws NoSuchAlgorithmException 无此算法异常
     * @throws InvalidKeySpecException
     */
    private PublicKey generatePublicKey(String publicKeyBase64) {
        PublicKey publicKey = null;
        try {
            byte[] buffer = Base64.decode(publicKeyBase64);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            publicKey = keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            logger.error("", e);
        } catch (InvalidKeySpecException e) {
            logger.error("", e);
        }
        return publicKey;
    }

    /**
     * 加载私钥
     *
     * @param privateKeyBase64
     * @return
     * @throws NoSuchAlgorithmException 无此算法异常
     * @throws InvalidKeySpecException
     */
    private PrivateKey generatePrivateKey(String privateKeyBase64) {
    	PrivateKey privateKey = null;
        try {
        	byte[] buffer = Base64.decode(privateKeyBase64);

            ASN1EncodableVector v = new ASN1EncodableVector();
            v.add(new ASN1Integer(0));
            ASN1EncodableVector v2 = new ASN1EncodableVector();
            v2.add(new ASN1ObjectIdentifier(PKCSObjectIdentifiers.rsaEncryption.getId()));
            v2.add(DERNull.INSTANCE);
            v.add(new DERSequence(v2));
            v.add(new DEROctetString(buffer));
            ASN1Sequence seq = new DERSequence(v);
            byte[] privKey = seq.getEncoded("DER");

            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privKey);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            privateKey = keyFactory.generatePrivate(keySpec);

        } catch (NoSuchAlgorithmException e) {
            logger.error("", e);
        } catch (InvalidKeySpecException e) {
            logger.error("", e);
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
        return privateKey;
    }
}
