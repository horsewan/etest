package com.eningqu.common.kit;

import cn.hutool.core.util.IdUtil;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * @author Yanghuangping
 * @version 1.0
 * @desc TODO
 * @since 2018/8/13 18:09
 **/
@Component
public class QRCodeKit {

    @Autowired
    private QiNiuKit qiNiuKit;

    public static BitMatrix encode() throws WriterException, IOException {
        String url = "www.eningqu.com";
        int width = 200; // 图像宽度
        int height = 200; // 图像高度
        String format = "png";// 图像类型
        Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        BitMatrix bitMatrix = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE, width, height, hints);
        return bitMatrix;
    }

    /**
     * 仅生成二维码，返回二维码图片网络地址，可直接扫码访问
     *
     * @param context 密文
     * @return
     */
    public String getQRCode(String path, String context) throws IOException, WriterException {
        String filePostfix = "png";
        String today = DateTimeKit.format(new Date(), DateTimeKit.DATE_PATTERN);// "yyyyMMdd";//以日期分文件夹
        String fileName = UUID.randomUUID().toString().replaceAll("-", "") + "." + filePostfix;
        String physicalPath = path + today + fileName;//保存在本地的地址
        File file = new File(physicalPath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        return encode(context, file, filePostfix, BarcodeFormat.QR_CODE, 500, 500, null);
    }

    private static final int BLACK = 0xff000000;
    private static final int WHITE = 0xFFFFFFFF;

    /**
     * 生成QRCode二维码<br>
     * 在编码时需要将com.google.zxing.qrcode.encoder.Encoder.java中的<br>
     * static final String DEFAULT_BYTE_MODE_ENCODING = "ISO8859-1";<br>
     * 修改为UTF-8，否则中文编译后解析不了<br>
     *
     * @param contents    二维码的内容
     * @param file        二维码保存的路径，如：C://test_QR_CODE.png
     * @param filePostfix 生成二维码图片的格式：png,jpeg,gif等格式
     * @param format      qrcode码的生成格式
     * @param width       图片宽度
     * @param height      图片高度
     * @param hints
     */
    public String encode(String contents, File file, String filePostfix, BarcodeFormat format, int width, int height, Map<EncodeHintType, ?> hints) throws WriterException, IOException {
        BitMatrix bitMatrix = new MultiFormatWriter().encode(contents, format, width, height);
        return writeToFile(bitMatrix, filePostfix, file);
    }

    /**
     * 生成二维码图片<br>
     *
     * @param matrix
     * @param format 图片格式
     * @param file   生成二维码图片位置
     * @throws IOException
     */
    public String writeToFile(BitMatrix matrix, String format, File file) throws IOException {
        BufferedImage image = toBufferedImage(matrix);
        //本地缓存图片
        ImageIO.write(image, format, file);

        //上传到七牛
        String fileName = IdUtil.simpleUUID() + format;
        qiNiuKit.upload(new FileInputStream(file), fileName);
        return fileName;
    }

    /**
     * 生成二维码内容<br>
     *
     * @param matrix
     * @return
     */
    public static BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) == true ? BLACK : WHITE);
            }
        }
        return image;
    }

    /**
     * 解析QRCode二维码
     */
    @SuppressWarnings("unchecked")
    public static void decode(File file) {
        try {
            BufferedImage image;
            try {
                image = ImageIO.read(file);
                if (image == null) {
                    System.out.println("Could not decode image");
                }
                LuminanceSource source = new BufferedImageLuminanceSource(image);
                BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
                Result result;
                @SuppressWarnings("rawtypes")
                Hashtable hints = new Hashtable();
                //解码设置编码方式为：utf-8
                hints.put(DecodeHintType.CHARACTER_SET, "utf-8");
                result = new MultiFormatReader().decode(bitmap, hints);
                String resultStr = result.getText();
                System.out.println("解析后内容：" + resultStr);
            } catch (IOException ioe) {
                System.out.println(ioe.toString());
            } catch (ReaderException re) {
                System.out.println(re.toString());
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }
}
