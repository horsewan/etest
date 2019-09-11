package com.eningqu;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.DbType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

/**
 * @description
 * @author H.P.YANG
 * @date 2017/12/21 17:00
 */

public class MpGenerator {

    public static void generateByTables(String packageName, String... tableNames){

        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig
                .setActiveRecord(true)              //不需要ActiveRecord特性的请改为false
                .setAuthor("WangQiang")         //作者
                .setOutputDir("E:\\mybatis-generator")             //输出路径
                .setFileOverride(true)
                .setEnableCache(false)              // XML 二级缓存
                .setBaseResultMap(true)             // XML ResultMap
                .setBaseColumnList(false);          // XML columList

        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDbType(DbType.MYSQL)
                .setUrl("jdbc:mysql://192.168.1.252:3306/eningqu_platform")
                .setUsername("root")
                .setPassword("123456")
                .setDriverName("com.mysql.jdbc.Driver");

        /**策略*/
        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig
                .setTablePrefix(new String[] { "fy_",})
                .setCapitalMode(true)  //首字母大写模式
                .setEntityLombokModel(false)  //lombok模式
                .setDbColumnUnderline(true)  //下划线转驼峰
                .setNaming(NamingStrategy.underline_to_camel)
                .setInclude(tableNames)       //修改替换成你需要的表名，多个表名传数组
                .setEntityBuilderModel(false)  //是否为构建者模型（默认 false）
                .setSuperMapperClass("com.eningqu.common.base.mapper.CrudDao")
                .setSuperEntityClass("com.eningqu.common.base.entity.BaseEntity") //自定义实体父类
                .setSuperServiceClass("com.eningqu.common.base.service.IBaseService")
                .setSuperServiceImplClass("com.eningqu.common.base.service.impl.BaseServiceImpl")
                .setSuperControllerClass("com.eningqu.common.base.controller.SysBaseController")
                .setSuperEntityColumns(new String[] {"id", "create_id", "create_time", "update_id", "update_time"});  //父类有的字段

        new AutoGenerator().setGlobalConfig(globalConfig)
                .setDataSource(dataSourceConfig)
                .setStrategy(strategyConfig)
                .setPackageInfo(
                        new PackageConfig()
                                .setParent(packageName)
                                .setController("controller")
                                .setService("service")
                                .setServiceImpl("service.impl")
                                .setMapper("mapper")
                                .setEntity("domain.api")
                ).execute();
    }

    public static void main(String[] args) {
    	System.out.println("-----------------------------------");
        String packageName = "com.eningqu";
        String[] tablesName = {"nq_smart_book_info","nq_smart_book_detail_info","nq_smart_book_resource_info"};
        generateByTables(packageName, tablesName);
    }

}
