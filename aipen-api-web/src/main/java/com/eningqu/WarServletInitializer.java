package com.eningqu;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * @desc TODO  发布war包时专用
 * @author Yanghuangping
 * @date 2018/4/17 15:42
 * @version 1.0
 **/
public class WarServletInitializer extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(ApiWebApplication.class);
    }
}
