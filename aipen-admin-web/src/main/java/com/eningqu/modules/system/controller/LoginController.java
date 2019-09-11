package com.eningqu.modules.system.controller;

import cn.hutool.core.util.StrUtil;

import com.eningqu.common.shiro.ShiroKit;
import com.eningqu.common.base.vo.RJson;
import com.eningqu.common.constant.Global;
import com.eningqu.common.enums.ResultEnum;
import com.eningqu.common.exception.AipenException;
import com.eningqu.common.kit.*;
import com.eningqu.domain.system.SysLog;
import com.eningqu.domain.system.SysUser;
import com.eningqu.modules.system.service.ISysLogService;
import com.eningqu.modules.system.service.ISysUserService;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.codec.Hex;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletResponse;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @desc TODO  登录controller
 * @author     Yanghuangping
 * @date       2018/4/25 16:33
 * @version    1.0
 *
 **/
@Controller
public class LoginController{

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ValidateCode validateCode;
    @Autowired
    private ISysLogService sysLogService;
    @Autowired
    private ISysUserService sysUserService;
    @Value("${jlc.url.android:https://www.pgyer.com/XEYY}")
    private String jlcAndroid;
    @Value("${jlc.url.ios:https://itunes.apple.com/app/id1448098630}")
    private String jlcIos;

    /**
     * TODO 默认访问
     * @return
     */
    @GetMapping({"", "/"})
    public void index(HttpServletResponse response){
        if(!ShiroKit.isLogin()){
            WebKit.redirectURL(response, "/login");
            return;
        }
        WebKit.redirectURL(response, "/index");
        return;
    }

    /**
     * TODO 首页
     * @param model
     * @return
     */
    @GetMapping("/index")
    public String index(Model model){
        model.addAttribute("login", ShiroKit.loginInfo());
        return "index";
    }

    /**
     * TODO 登录页
     * @return
     */
    @GetMapping("/login")
    public String login(){
        return "login";
    }


    /**
     * TODO 用户登录
     * @param username
     * @param password
     * @param code
     * @return
     */
    @PostMapping("/login")
    @ResponseBody
    public RJson login(@RequestParam(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM) String username,
                       @RequestParam(FormAuthenticationFilter.DEFAULT_PASSWORD_PARAM) String password,
                       @RequestParam("code") String code){

        String sessioCode = (String) ShiroKit.getSessionAttribute(Global.VERIFY_CODE);
        // 校验验证码
        if(!StringUtils.isBlank(sessioCode) && !StringUtils.equalsIgnoreCase(code, sessioCode)){
            return RJson.error().setCode(RJson.UNAUTHORIZED).setMsg("验证码错误");
        }
        try {
            Subject subject = ShiroKit.getSubject();
            UsernamePasswordToken token = new UsernamePasswordToken(username, password);
            // 校验账号密码
            subject.login(token);
        } catch (UnknownAccountException e) {
            logger(username, ResultEnum.ERROR.getValue(), e.getMessage());
            throw e;
        } catch (LockedAccountException e) {
            logger(username, ResultEnum.ERROR.getValue(), e.getMessage());
            throw e;
        } catch (AuthenticationException e) {
            logger(username, ResultEnum.ERROR.getValue(), e.getMessage());
            throw e;
        }
        logger(username, ResultEnum.SUCCESS.getValue(), null);
        return RJson.ok().setMsg("/index");
    }

    /**
     * TODO 验证码
     * @param response
     */
    @GetMapping(value = "/captcha")
    public void captcha(HttpServletResponse response){
        OutputStream out = null;
        try {
            //设置输出的类型为图片
            response.setContentType("image/jpeg");

            //设置不进行缓存
            response.setHeader("pragma", "no-cache");
            response.setHeader("cache-control", "no-cache");
            response.setHeader("expires", "0");
            response.setHeader("Transfer-Encoding","jpeg");

            BufferedImage bufferedImage = validateCode.getImage();
            ShiroKit.setSessionAttribute(Global.VERIFY_CODE, validateCode.getText());
            out = response.getOutputStream();
            ValidateCode.output(bufferedImage, out);
        } catch (Exception e) {
            logger.error("图片验证码，{}", e);
        } finally {
            if(out != null){
                try {
                    out.close();
                } catch (IOException e) {
                    logger.error("图片验证码，{}", e);
                }
            }
        }
    }
    /**
     * 佳乐成下载地址维护
     * @return
     */
    @GetMapping(value = "/getAppUrl")
//    @ResponseBody
    public ModelAndView getAppUrl(){
    	
    	boolean ios = UserAgentKit.isIOSDevice(WebKit.getRequest());
    	boolean mobile = UserAgentKit.isMobileDevice(WebKit.getRequest());
    	if(mobile && ios){
    		return  new ModelAndView(new RedirectView(jlcIos));
    	}else if(mobile){
    		return  new ModelAndView(new RedirectView(jlcAndroid));
    	}else{
    		return  new ModelAndView(new RedirectView(jlcAndroid));
    	}
    	/*JSONObject object = new JSONObject();
    	object.put("ios", ios);
    	object.put("mobile", mobile);
    	return RJson.ok().setData(object);*/
    }

    /**
     * TODO 解锁
     * @param password
     * @return
     */
    @PostMapping("/unlock")
    @ResponseBody
    public RJson unlock(@RequestParam String password){
        SysUser sysUser = sysUserService.selectById(ShiroKit.id());
        byte[] salt = Hex.decode(sysUser.getLoginPwd().substring(0, 16));
        if(!StrUtil.equalsIgnoreCase(PasswordKit.entrypt(password, salt), sysUser.getLoginPwd())){
            throw new AipenException("解锁密码不正确");
        }
        return RJson.ok("解锁成功");
    }

    @GetMapping("/403")
    public String error403(){
        return "403";
    }

    @GetMapping("/404")
    public String error404(){
        return "404";
    }

    @GetMapping("/500")
    public String error500(){
        return "500";
    }

    private void logger(String loginName, String result, String exception){
        SysLog log = new SysLog();
        log.setIp(IPKit.getIpAddr(WebKit.getRequest()));
        log.setLoginName(loginName);
        log.setCreateTime(DateTimeKit.getDate());
        log.setFunc("系统用户登录");
        log.setUrl("/login");
        log.setResult(result);
        log.setException(exception);
        sysLogService.insert(log);
    }
}
