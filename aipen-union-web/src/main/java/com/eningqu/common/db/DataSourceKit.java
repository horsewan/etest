package com.eningqu.common.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @desc TODO  
 * @author     Yanghuangping
 * @date       2018/4/28 14:45
 * @version    1.0
 *
 **/
public class DataSourceKit {

    private final static Logger logger = LoggerFactory.getLogger(DataSourceKit.class);

    public final static String MASTER = "MASTER";
    public final static String SLAVE = "SLAVE";

    private static final ThreadLocal<String> dbContextHolder = new InheritableThreadLocal<>();

    /**
     * TODO 切换数据源
     * @param dataSourceName
     */
    public static void setDataSource(String dataSourceName) {
        logger.debug("---------- 切换数据源 -------------", dataSourceName);
        dbContextHolder.set(dataSourceName);
    }

    /**
     * TODO 获取数据源名
     *
     * @return
     */
    public static String getDataSource() {
        return (dbContextHolder.get());
    }

    /**
     * TODO 清除数据源名
     */
    public static void clearDataSource() {
        dbContextHolder.remove();
    }

}
