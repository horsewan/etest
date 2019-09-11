package com.eningqu.common.config;

import com.eningqu.common.interceptor.EncodeInterceptor;
import com.eningqu.common.xss.XssFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.Arrays;

/**
 *
 * @desc TODO  JWT验证拦截器
 * @author     Yanghuangping
 * @date       2018/4/27 17:28
 * @version    1.0
 *
 **/
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    /**
     * 请求URL过滤
     */
    private final static String[] EXCLUDE_PATH_PATTERNS = {
            "/error",
            "/api/error",
            "/api/union/**"
    };

    @Autowired
    private EncodeInterceptor encodeInterceptor;
//    @Autowired
//    private JwtInterceptor jwtInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(encodeInterceptor)
                .addPathPatterns("/**");

//        registry.addInterceptor(jwtInterceptor)
//                .addPathPatterns("/**")
//                .excludePathPatterns(EXCLUDE_PATH_PATTERNS);

        super.addInterceptors(registry);
    }

    /**
     * TODO 允许跨域配置
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowCredentials(true)
                .allowedMethods("GET", "POST")
                .maxAge(3600);
    }


    /**
     * TODO xssFilter注册
     */
    @Bean
    public FilterRegistrationBean xssFilterRegistration() {
        XssFilter xss = new XssFilter();
        xss.setUrlExclusion(Arrays.asList());
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setOrder(Integer.MAX_VALUE);
        filterRegistrationBean.setFilter(xss);
        return filterRegistrationBean;
    }

    /** 静态授权文件地址 */
    @Value("${auth.path}")
    private String authPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/audio/**").addResourceLocations("file:" + authPath);
    }


}
