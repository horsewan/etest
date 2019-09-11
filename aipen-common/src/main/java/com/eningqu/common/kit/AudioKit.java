package com.eningqu.common.kit;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 描    述：  TODO
 * 作    者：  YangHuangPing
 * 邮    箱：  171341296@qq.com
 * 日    期：  2019/1/19 14:31
 */
public final class AudioKit {

    private AudioKit() {
    }

    public static byte[] pcm2wav(InputStream inputStream) {

        if (inputStream == null) {
            return null;
        }

        ByteArrayOutputStream byteArrayOut = null;

        try {
            byte[] byteIn = IOUtils.toByteArray(inputStream);
            if (byteIn == null && byteIn.length == 0) {
                return null;
            }

            int TOTAL_SIZE = byteIn.length;

            WavHeader wav = new WavHeader();
            // 长度字段 = 内容的大小（TOTAL_SIZE) + 头部字段的大小(不包括前面4字节的标识符RIFF以及fileLength本身的4字节)
            wav.fileLength = TOTAL_SIZE + (44 - 8);
            wav.FmtHdrLeth = 16;
            wav.BitsPerSample = 16;
            wav.Channels = 1;
            wav.FormatTag = 0x0001;
            wav.SamplesPerSec = 16000;
            wav.BlockAlign = (short) (wav.Channels * wav.BitsPerSample / 8);
            wav.AvgBytesPerSec = wav.BlockAlign * wav.SamplesPerSec;
            wav.DataHdrLeth = TOTAL_SIZE;

            byte[] header = header = wav.getHeader();
            // WAV标准，头部应该是44字节,如果不是44个字节则不进行转换文件
            if (header.length != 44) {
                return null;
            }

            byteArrayOut = new ByteArrayOutputStream(header.length + byteIn.length);

            byteArrayOut.write(header);
            byteArrayOut.write(byteIn);
            return byteArrayOut.toByteArray();
        } catch (IOException e) {

        } finally {
            if (byteArrayOut != null) {
                try {
                    byteArrayOut.close();
                } catch (IOException e) {
                }
            }
        }
        return null;
    }
}
