package com.eningqu.common.ueditor;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import com.eningqu.common.config.QiNiuConfig;
import com.eningqu.common.exception.AipenException;
import com.eningqu.common.kit.QiNiuKit;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 *
 * @desc TODO  百度富文本框编辑器专用
 * @author     Yanghuangping
 * @since      2018/7/20 18:07
 * @version    1.0
 *
 **/
@Controller
public class UeditorController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private QiNiuConfig qiNiuConfig;
    @Autowired
    private QiNiuKit qiNiuKit;

    @RequestMapping(value="/ueditor/config", produces = "application/json;charset=utf-8")
    public void config(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json");
        String rootPath = request.getSession().getServletContext().getRealPath("/");
        try {
            String exec = new ActionEnter(request, rootPath).exec();
            PrintWriter writer = response.getWriter();
            writer.write(exec);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            logger.error("ueditor插件加载配置文件失败", e);
        }
    }

    /**
     * TODO 百度ueditor富文本插件专用上传文件地址
     */
    @PostMapping(value = "/ueditor/upload", produces = "application/json;charset=utf-8")
    public void uploadFile( @RequestParam("upfile") MultipartFile uploadFile, HttpServletResponse response) {

        Map<String, String> map = Maps.newHashMap();

        if(uploadFile == null){
            logger.error("未获取到文件，上传失败");
            throw new AipenException("上传失败");
        }

        map.put("original", uploadFile.getOriginalFilename());
        map.put("size", uploadFile.getSize() + "");

        // 文件后缀
        String fileSuffix = uploadFile.getOriginalFilename().substring(uploadFile.getOriginalFilename().lastIndexOf("."));
        String fileName = IdUtil.simpleUUID() + fileSuffix;
        try {
            qiNiuKit.upload(uploadFile.getInputStream(), fileName);
        } catch (IOException e) {
            logger.error("文件上传七牛云空间失败,{}", e);
            throw new AipenException("上传失败");
        }

        map.put("type", fileSuffix);
        map.put("title", fileName);
        map.put("url", qiNiuConfig.getDomain() + fileName);

        try {
            map.put("state", "SUCCESS");
            PrintWriter writer = response.getWriter();
            writer.write(JSONUtil.toJsonStr(map));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            logger.error("ueditor插件上传文件失败", e);
        }
    }
}
