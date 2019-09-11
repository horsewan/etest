package com.eningqu.common.kit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 *
 * @desc TODO  
 * @author     Yanghuangping
 * @date       2018/4/27 14:04
 * @version    1.0
 *
 **/
public class ByteKit {

    private final static Logger logger = LoggerFactory.getLogger(ByteKit.class);

    public static byte[] toByte(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof String) {
            return ((String) obj).getBytes();
        }
        byte[] bytes = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            bytes = bos.toByteArray();
            oos.close();
            bos.close();
        } catch (IOException ex) {
            logger.error("", ex);
        }
        return bytes;
    }

    public static <T> T toObject(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        T obj = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bis);
            obj = (T) ois.readObject();
            ois.close();
            bis.close();
        } catch (IOException ex) {
            logger.error("", ex);
        } catch (ClassNotFoundException ex) {
            logger.error("", ex);
        }
        return obj;
    }

    public static byte[] merger(byte[]... values) {
        int length_byte = 0;
        for (int i = 0; i < values.length; i++) {
            length_byte += values[i].length;
        }
        byte[] all_byte = new byte[length_byte];
        int countLength = 0;
        byte[] b;
        for (int i = 0; i < values.length; i++) {
            b = values[i];
            System.arraycopy(b, 0, all_byte, countLength, b.length);
            countLength += b.length;
        }
        return all_byte;
    }

    public static InputStream byte2Stream(byte[] buf){
        return new ByteArrayInputStream(buf);
    }

    public static byte[] stream2Byte(InputStream inputStream) {
        if (inputStream == null) {
            return null;
        }
        ByteArrayOutputStream swapStream = null;
        try {
            swapStream = new ByteArrayOutputStream();
            byte[] buff = new byte[1024];
            int rc = 0;
            while ((rc = inputStream.read(buff, 0, 100)) > 0) {
                swapStream.write(buff, 0, rc);
            }
            byte[] in_b = swapStream.toByteArray();
        } catch (IOException e) {

        } finally {
            if (swapStream != null) {
                try {
                    swapStream.close();
                } catch (IOException e) {
                }
            }
        }
        return null;
    }
}
