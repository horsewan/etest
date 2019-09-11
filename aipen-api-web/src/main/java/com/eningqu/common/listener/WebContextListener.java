package com.eningqu.common.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;

/**
 *
 * @desc TODO  在启动Web容器时，自动装配Spring applicationContext.xml的配置信息。
 * 注：DispatcherServlet的上下文仅仅是Spring MVC的上下文，而Spring加载的上下文是通过ContextLoaderListener来加载的。
 * 一般spring web项目中同时会使用这两种上下文，
 * 前者仅负责MVC相关bean的配置管理（如ViewResolver、Controller、MultipartResolver等），
 * 后者则负责整个spring相关bean的配置管理（如相关Service、DAO等）
 * @author     Yanghuangping
 * @since      2018/7/30 11:04
 * @version    1.0
 *
 **/
@WebListener
public class WebContextListener extends ContextLoaderListener{

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void contextInitialized(ServletContextEvent event) {
        //获取Servlet上下文
        ServletContext sct = event.getServletContext();
        //获取Spring应用上下文
        ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(sct);
        initSystemParams(sct);
        logger.debug(".........startup listener........");
    }

    /**
     * TODO 初始化系统参数
     *
     * @param servletContext
     */
    private void initSystemParams(ServletContext servletContext) {
        // TODO 系统配置信息--从缓存中获取。
    }
}
