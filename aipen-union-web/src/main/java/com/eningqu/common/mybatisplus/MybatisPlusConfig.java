package com.eningqu.common.mybatisplus;

import com.baomidou.mybatisplus.enums.DBType;
import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.plugins.PerformanceInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @desc TODO  mybatis-plus配置类
 * @author 	   Yanghuangping
 * @date       2018/4/17 16:25
 * @version    1.0
 **/
@Configuration
@MapperScan(basePackages = {"com.eningqu.mapper"})
public class MybatisPlusConfig {


	/***
	 * mybatis-plus SQL执行效率插件【生产环境可以关闭】
	 * @return
     */
	@Bean
	@Profile("dev")
	public PerformanceInterceptor performanceInterceptor() {
		PerformanceInterceptor performanceInterceptor=new PerformanceInterceptor();
		/*<!-- SQL 执行性能分析，开发环境使用，线上不推荐。 maxTime 指的是 sql 最大执行时长 -->*/
		performanceInterceptor.setMaxTime(1000);
		/*<!--SQL是否格式化 默认false-->*/
		performanceInterceptor.setFormat(true);
		return performanceInterceptor;
	}

	/**
	 *	 mybatis-plus分页插件
	 */
	@Bean
	public PaginationInterceptor paginationInterceptor() {
		PaginationInterceptor page = new PaginationInterceptor();
		page.setDialectType(DBType.MYSQL.getDb());
		return page;
	}
}
