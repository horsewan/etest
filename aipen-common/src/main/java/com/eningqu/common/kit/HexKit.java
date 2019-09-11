package com.eningqu.common.kit;

import cn.hutool.core.util.StrUtil;

import java.nio.charset.Charset;

/**
 *
 * @desc TODO  
 * @author     Yanghuangping
 * @date       2018/5/11 11:10
 * @version    1.0
 *
 **/
public final class HexKit {

    private static final char[] DIGITS_LOWER = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static final char[] DIGITS_UPPER = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    private static final Charset charset = Charset.forName("UTF-8");

    public HexKit() {
    }

    public static boolean isHexNumber(String value) {
        int index = value.startsWith("-") ? 1 : 0;
        return value.startsWith("0x", index) || value.startsWith("0X", index) || value.startsWith("#", index);
    }

    public static char[] encodeHex(byte[] data) {
        return encodeHex(data, true);
    }

    public static char[] encodeHex(String str, Charset charset) {
        byte[] data = charset == null ? str.getBytes() : str.getBytes(charset);
        return encodeHex(data, true);
    }

    public static char[] encodeHex(byte[] data, boolean toLowerCase) {
        return encodeHex(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
    }

    public static String encodeHexStr(byte[] data) {
        return encodeHexStr(data, true);
    }

    public static String encodeHexStr(String data, Charset charset) {
        return encodeHexStr(StrUtil.bytes(data, charset), true);
    }

    public static String encodeHexStr(String data) {
        return encodeHexStr(data, charset);
    }

    public static String encodeHexStr(byte[] data, boolean toLowerCase) {
        return encodeHexStr(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
    }

    public static String decodeHexStr(String hexStr) {
        return decodeHexStr(hexStr, charset);
    }

    public static String decodeHexStr(String hexStr, Charset charset) {
        return StrUtil.isEmpty(hexStr) ? hexStr : decodeHexStr(hexStr.toCharArray(), charset);
    }

    public static String decodeHexStr(char[] hexData, Charset charset) {
        return StrUtil.str(decodeHex(hexData), charset);
    }

    public static byte[] decodeHex(char[] hexData) {
        int len = hexData.length;
        if ((len & 1) != 0) {
            throw new RuntimeException("Odd number of characters.");
        } else {
            byte[] out = new byte[len >> 1];
            int i = 0;

            for(int j = 0; j < len; ++i) {
                int f = toDigit(hexData[j], j) << 4;
                ++j;
                f |= toDigit(hexData[j], j);
                ++j;
                out[i] = (byte)(f & 255);
            }

            return out;
        }
    }

    public static byte[] decodeHex(String hexStr) {
        return StrUtil.isEmpty(hexStr) ? null : decodeHex(hexStr.toCharArray());
    }

    public static String toUnicodeHex(int value) {
        StringBuilder builder = new StringBuilder(6);
        builder.append("\\u");
        String hex = Integer.toHexString(value);
        int len = hex.length();
        if (len < 4) {
            builder.append("0000", 0, 4 - len);
        }

        builder.append(hex);
        return builder.toString();
    }

    public static String toUnicodeHex(char ch) {
        StringBuilder sb = new StringBuilder(6);
        sb.append("\\u");
        sb.append(DIGITS_LOWER[ch >> 12 & 15]);
        sb.append(DIGITS_LOWER[ch >> 8 & 15]);
        sb.append(DIGITS_LOWER[ch >> 4 & 15]);
        sb.append(DIGITS_LOWER[ch & 15]);
        return sb.toString();
    }

    private static String encodeHexStr(byte[] data, char[] toDigits) {
        return new String(encodeHex(data, toDigits));
    }

    private static char[] encodeHex(byte[] data, char[] toDigits) {
        int l = data.length;
        char[] out = new char[l << 1];
        int i = 0;

        for(int var5 = 0; i < l; ++i) {
            out[var5++] = toDigits[(240 & data[i]) >>> 4];
            out[var5++] = toDigits[15 & data[i]];
        }

        return out;
    }

    private static int toDigit(char ch, int index) {
        int digit = Character.digit(ch, 16);
        if (digit == -1) {
            throw new RuntimeException("Illegal hexadecimal character " + ch + " at index " + index);
        } else {
            return digit;
        }
    }
}
