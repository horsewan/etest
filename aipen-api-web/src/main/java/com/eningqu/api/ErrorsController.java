package com.eningqu.api;

import com.eningqu.common.base.vo.RJson;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @desc TODO  
 * @author     Yanghuangping
 * @since      2018/11/21 18:39
 * @version    1.0
 *
 **/
@Controller
@RequestMapping("/api/error")
public class ErrorsController implements ErrorController{


    @RequestMapping(produces = {"text/html"})
    public void errorHTML() {}

    @RequestMapping
    @ResponseBody
    public RJson errorJSON() {
        return RJson.error("请求接口错误");
    }

    @Override
    public String getErrorPath() {
        return "/api/error";
    }
}
