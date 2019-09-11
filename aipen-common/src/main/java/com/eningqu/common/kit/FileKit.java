package com.eningqu.common.kit;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 *
 * @desc TODO  
 * @author     Yanghuangping
 * @date       2018/5/11 15:46
 * @version    1.0
 *
 **/
public class FileKit {

    public static File saveFile(FileInputStream fis, String filePath){
        File saveFile = new File(filePath);

        if (!saveFile.getParentFile().exists()) {
            saveFile.getParentFile().mkdirs();
        }

        FileOutputStream fos = null;
        FileChannel inChannel = null;
        FileChannel outChannel = null;

        ByteBuffer buffer = ByteBuffer.allocate(1024);

        try {

            fos = new FileOutputStream(saveFile);
            inChannel = fis.getChannel();
            outChannel = fos.getChannel();

            while (inChannel.read(buffer) != -1){
                // 切换成读取数据模式
                buffer.flip();
                // 将缓冲区数据写入通道中
                outChannel.write(buffer);
                // 清除缓冲区
                buffer.clear();
            }

            // 这种方式也可行
            //inChannel.transferTo(0, inChannel.size(), outChannel);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e){
            e.printStackTrace();
            return null;
        } finally {

            if(outChannel != null){
                try {
                    outChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if(fos != null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if(inChannel != null){
                try {
                    inChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if(fis != null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return saveFile;
    }
}
