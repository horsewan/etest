package com.eningqu.modules.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * 描    述：  TODO  使用指南H5 用于app应用的操作说明
 * 作    者：  YangHuangPing
 * 邮    箱：  171341296@qq.com
 * 日    期：  2019/1/2 17:14
 *
 */
@Controller
@RequestMapping("/nq/guide")
public class GuideController {

    @GetMapping("/BL")
    public String blPage(){
        return "guide/bl_op_guide_cn";
    }

    @GetMapping("/daoyu")
    public String daoyuPage(){
        return "guide/daoyu";
    }

    @GetMapping("/about_daoyu")
    public String aboutDaoyuPage(){
        return "guide/about_daoyu";
    }

    @GetMapping("/{clientName}/{language}")
    public String page(@PathVariable String clientName,
                       @PathVariable String language){
        switch (clientName) {
            case "BL":
                switch (language) {
                    case "cn":
                        return "guide/bl_op_guide_cn";
                    case "cht":
                        return "guide/bl_op_guide_cht";
                    case "en":
                        return "guide/bl_op_guide_en";
                    case "ja":
                        return "guide/bl_op_guide_ja";
                    default:
                        return "guide/bl_op_guide_cn";
                }
            default:
                return "/404";
        }
    }



}
