package com.eningqu.common.kit;

import com.eningqu.common.constant.Global;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


/**
 *
 * @desc TODO  
 * @author     Yanghuangping
 * @date       2018/5/11 16:10
 * @version    1.0
 *
 **/
public class ZipKit {

    private static byte[] ZIP_HEADER_1 = new byte[] { 80, 75, 3, 4 };
    private static byte[] ZIP_HEADER_2 = new byte[] { 80, 75, 5, 6 };

    /**
     * 计算zip包原始大小
     * @param zipFile
     * @param charset
     * @return
     */
    public static long countSize(File zipFile, Charset charset){
        charset = null == charset ? Charset.forName("UTF-8") : charset;
        ZipFile zipFileObj = null;
        long size = 0;
        try {
            zipFileObj = new ZipFile(zipFile, charset);
            Enumeration<? extends ZipEntry> em = zipFileObj.entries();
            ZipEntry zipEntry = null;
            while(em.hasMoreElements()) {
                zipEntry = em.nextElement();
                if (!zipEntry.isDirectory()) {
                    size += zipEntry.getSize();
                }
                // 是压缩文件 则递归
                if (zipEntry.getName().endsWith(Global.FILE_SUFFIX)) {

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(zipFileObj != null) {
                try {
                    zipFileObj.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return size;
    }

    public static boolean isArchiveFile(File file){
        if(file == null){
            return false;
        }

        if (file.isDirectory()) {
            return false;
        }

        boolean isArchive = false;
        InputStream input = null;
        try {
            input = new FileInputStream(file);
            byte[] buffer = new byte[4];
            int length = input.read(buffer, 0, 4);
            if (length == 4) {
                isArchive = (Arrays.equals(ZIP_HEADER_1, buffer)) || (Arrays.equals(ZIP_HEADER_2, buffer));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                }
            }
        }

        return isArchive;
    }
}
