package com.eningqu.api;

import com.eningqu.common.kit.RedisKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@RestController
@RequestMapping("/api/uploadImg")
public class ZdUploadController {

    private static Logger logger = LoggerFactory.getLogger(ZdUploadController.class);

    @Autowired
    private RedisKit redisKit;



}
