package com.eningqu.common.kit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 描    述：  TODO   ping命令辅助类
 * 作    者：  YangHuangPing
 * 邮    箱：  171341296@qq.com
 * 日    期：  2019/1/17 18:26
 */
public class PingKit {

    private static final String SYSTEM = "linux";
    private static final int DEFAULT_PING_COUNT = 4;
    private static final String REGEX = "(\\d+ms)(\\s+)(TTL=\\d+)";

    private static final Logger logger = LoggerFactory.getLogger(PingKit.class);

    public static boolean ping(String ip) {
        return ping(ip, DEFAULT_PING_COUNT);
    }

    public static boolean ping(String ip, int pingTimes) {
        BufferedReader in = null;
        Runtime r = Runtime.getRuntime();
        String pingCommand = getPingCommand(ip);
        if (pingCommand == null || "".equals(pingCommand)) {
            return false;
        }
        try {
            Process p = r.exec(pingCommand);
            if (p == null) {
                return false;
            }
            in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            int connectedCount = 0;
            String line = null;
            while ((line = in.readLine()) != null) {
                // 如果出现类似=23ms TTL=62这样的字样,出现的次数=测试次数则返回真
                connectedCount += getCheckResult(line);
            }
            return connectedCount == pingTimes;

        } catch (IOException e) {
            logger.error("ping is failure, exception={}", e.getMessage());
            return false;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
            }
        }
    }


    private static String getPingCommand(String ip) {
        // 获取操作系统类型
        String pingCommand;
        if (isLinux()) {
            pingCommand = "ping -c 4 -i 0 " + ip;
        } else {
            pingCommand = "ping " + ip + " -n 4 -w 10";
        }
        return pingCommand;
    }

    public static boolean isLinux() {
        String osName = System.getProperty("os.name");
        return osName.toLowerCase().contains(SYSTEM);
    }

    public static boolean isWindows() {
        return !isLinux();
    }

    private static int getCheckResult(String line) {
        Pattern pattern = Pattern.compile(REGEX, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
            return 1;
        }
        return 0;
    }
}
