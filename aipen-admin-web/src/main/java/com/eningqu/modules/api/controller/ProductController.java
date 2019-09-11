package com.eningqu.modules.api.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.eningqu.common.annotation.Log;
import com.eningqu.common.config.QiNiuConfig;
import com.eningqu.common.kit.IdWorkerKit;
import com.eningqu.modules.system.service.ISysDictService;
import com.eningqu.common.base.vo.DataTable;
import com.eningqu.common.base.vo.RJson;
import com.eningqu.common.base.vo.ValidJson;
import com.eningqu.common.constant.SearchParam;
import com.eningqu.common.enums.ProductStatusEnum;
import com.eningqu.common.exception.AipenException;
import com.eningqu.common.kit.QiNiuKit;
import com.eningqu.common.kit.ValidationKit;
import com.eningqu.domain.api.Product;
import com.eningqu.modules.api.params.HongbaoParams;
import com.eningqu.modules.api.params.ProductParams;
import com.eningqu.modules.api.service.IProductService;
import com.google.common.collect.Maps;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.groups.Default;
import java.io.IOException;
import java.util.Map;

/**
 *
 * @desc TODO  产品
 * @author     Yanghuangping
 * @since      2018/7/18 11:15
 * @version    1.0
 *
 **/
@Controller
@RequestMapping("/mall/product")
public class ProductController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IProductService productService;
    @Autowired
    private ISysDictService sysDictService;
    @Autowired
    private QiNiuConfig qiNiuConfig;
    @Autowired
    private QiNiuKit qiNiuKit;

    @GetMapping("")
    @RequiresPermissions("mall:product:list")
    public String list(Model model){
//        model.addAttribute("oneClass", sysDictService.queryDictByGroupCode("product_one_class"));
        model.addAttribute("status", sysDictService.queryDictByGroupCode("product_status"));
        return "api/product/product_list";
    }

    //TODO 红包列表
    @GetMapping("/hongbao")
    @RequiresPermissions("mall:product:hongbaoList")
    public String hongbaoList(Model model){
//        model.addAttribute("oneClass", sysDictService.queryDictByGroupCode("product_one_class"));
        model.addAttribute("status", sysDictService.queryDictByGroupCode("product_status"));
        return "api/product/hongbao_list";
    }

    @GetMapping("/page")
    @RequiresPermissions("mall:product:edit")
    public String page(Model model){
        model.addAttribute("oneClass", sysDictService.queryDictByGroupCode("product_one_class"));
        return "api/product/product_page";
    }

    //TODO 红包编辑页面
    @GetMapping("/hongbaoPage")
    @RequiresPermissions("mall:product:hongbaoedit")
    public String hongbaoPage(Model model){
        model.addAttribute("oneClass", sysDictService.queryDictByGroupCode("product_one_class"));
        return "api/product/hongbao_page";
    }

    @GetMapping("/detail/{id}")
    @RequiresPermissions("mall:product:edit")
    public String detail(@PathVariable Long id, Model model){
        String html = productService.selectProductDetail(id);
        model.addAttribute("id", id);
        model.addAttribute("html", html);
        return "api/product/product_detail";
    }

    //TODO红包详情
    @GetMapping("/hongbaoDetail/{id}")
    @RequiresPermissions("mall:product:edit")
    public String hongbaoDetail(@PathVariable Long id, Model model){
        String html = productService.selectProductDetail(id);
        model.addAttribute("id", id);
        model.addAttribute("html", html);
        return "api/product/hongbao_detail";
    }

    /**
     * TODO 查询产品信息
     * @param page
     * @param limit
     * @param productNo
     * @param productName
     * @param status
     * @return
     */
    @GetMapping(value = "/dataTable")
    @ResponseBody
    @RequiresPermissions("mall:product:list")
    @Log("查询产品数据信息列表")
    public DataTable<Product> dataTable(@RequestParam int page,
                                        @RequestParam int limit,
                                        @RequestParam(required = false) String productNo,
                                        @RequestParam(required = false) String productName,/*
                                        @RequestParam(required = false) String oneClass,*/
                                        @RequestParam(required = false) String status){

        Map<String, Object> searchParams = Maps.newHashMap();
        searchParams.put(SearchParam.SEARCH_EQ + "product_no", productNo);
        searchParams.put(SearchParam.SEARCH_RLIKE + "product_name", productName);
        searchParams.put(SearchParam.SEARCH_EQ + "one_class", 2);
        searchParams.put(SearchParam.SEARCH_EQ + "status", status);

        DataTable<Product> dataTable = new DataTable();
        dataTable.setFields(new String[]{"id","product_no","product_name","product_desc","one_class","original_price","sale_price","thumb_pic_url","status","stock_quantity","sold_quantity","product_detail","create_time","update_time"});
        dataTable.setPage(page);
        dataTable.setLimit(limit);
        dataTable.setSearchParams(searchParams);

        productService.pageSearch(dataTable);

        return dataTable;
    }

    /**
     * TODO 查询红包列表信息
     * @param page
     * @param limit
     * @param productNo
     * @param productName
     * @return
     */
    @GetMapping(value = "/hongbaoDataTable")
    @ResponseBody
    @RequiresPermissions("mall:product:hongbaoList")
    @Log("查询红包数据信息列表")
    public DataTable<Product> hongbaoDataTable(@RequestParam int page,
                                        @RequestParam int limit,
                                        @RequestParam(required = false) String productNo,
                                        @RequestParam(required = false) String productName/*,
                                        @RequestParam(required = false) String oneClass,
                                        @RequestParam(required = false) String status*/){

        Map<String, Object> searchParams = Maps.newHashMap();
        searchParams.put(SearchParam.SEARCH_EQ + "product_no", productNo);
        searchParams.put(SearchParam.SEARCH_RLIKE + "product_name", productName);
        searchParams.put(SearchParam.SEARCH_EQ + "one_class", 1);
//        searchParams.put(SearchParam.SEARCH_EQ + "status", status);

        DataTable<Product> dataTable = new DataTable();
        dataTable.setFields(new String[]{"id","product_no","product_name","product_desc","one_class","original_price","sale_price","thumb_pic_url","status","stock_quantity","sold_quantity","create_time","update_time"});
        dataTable.setPage(page);
        dataTable.setLimit(limit);
        dataTable.setSearchParams(searchParams);

        productService.pageSearch(dataTable);

        return dataTable;
    }

    /**
     * TODO 新增产品
     * @param productParams
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    @RequiresPermissions("mall:product:add")
    @Log("新增产品数据")
    public RJson add(ProductParams productParams){

        ValidJson result = ValidationKit.validate(productParams, Default.class);
        if(result.hasErrors()){
            logger.error("产品添加失败，{}", result.getMessage());
            throw new AipenException("添加失败");
        }
        Product product = new Product();
        BeanUtil.copyProperties(productParams, product);
        product.setProductNo(IdWorkerKit.uniqueId(7));
        product.setStatus(ProductStatusEnum.ON_SHELVE.getValue());
        //salePrice
        product.setProductDetail(productParams.getProductDetail());
        productService.insert(product);
        return RJson.ok("添加成功");
    }
    /**
     * TODO 新增红包
     * @param productParams
     * @return
     */
    @PostMapping("/hongbaoAdd")
    @ResponseBody
    @RequiresPermissions("mall:product:hongbaoadd")
    @Log("新增红包数据")
    public RJson hongbaoAdd(HongbaoParams productParams){

        ValidJson result = ValidationKit.validate(productParams, Default.class);
        if(result.hasErrors()){
            logger.error("产品添加失败，{}", result.getMessage());
            return RJson.error("红包数据录入有误");
        }
        Product product = new Product();
        BeanUtil.copyProperties(productParams, product);
        product.setProductNo(IdWorkerKit.uniqueId(7));
        product.setStatus(ProductStatusEnum.ON_SHELVE.getValue());
        productService.insert(product);
        return RJson.ok("添加成功");
    }


    /**
     * TODO 更新产品
     * @param productParams
     * @return
     */
    @PostMapping("/edit")
    @ResponseBody
    @RequiresPermissions("mall:product:edit")
    @Log("更新产品数据")
    public RJson edit(ProductParams productParams){
        ValidJson result = ValidationKit.validate(productParams, Default.class, ValidationKit.EditValid.class);
        if(result.hasErrors()){
            logger.error("产品更新失败，{}", result.getMessage());
            return RJson.error("更新失败");
        }
        Product product = new Product();
        BeanUtil.copyProperties(productParams, product);
        productService.updateById(product);
        return RJson.ok("更新成功");
    }
    /**
     * TODO 更新红包
     * @param productParams
     * @return
     */
    @PostMapping("/hongbaoEdit")
    @ResponseBody
    @RequiresPermissions("mall:product:hongbaoedit")
    @Log("更新红包数据")
    public RJson hongbaoEdit(HongbaoParams productParams){
        ValidJson result = ValidationKit.validate(productParams, Default.class, ValidationKit.EditValid.class);
        if(result.hasErrors()){
            logger.error("产品更新失败，{}", result.getMessage());
            return RJson.error("红包数据录入有误");
        }
        Product product = new Product();
        BeanUtil.copyProperties(productParams, product);
        productService.updateById(product);
        return RJson.ok("更新成功");
    }


    /**
     * TODO 更新产品状态
     * @param id
     * @param status
     * @return
     */
    @GetMapping("/status/{id}/{status}")
    @ResponseBody
    @RequiresPermissions("mall:product:edit")
    @Log("更新产品状态")
    public RJson status(@PathVariable Long id, @PathVariable String status){
        String key = ProductStatusEnum.getKey(status);
        if(StrUtil.isBlank(key)){
            logger.error("更新产品状态码失败,{}", id, status);
            throw new AipenException("产品状态码错误");
        }
        productService.updateStatus(id, status);
        return RJson.ok("更新成功");
    }

    @GetMapping("/hongbaoDelete/{id}")
    @ResponseBody
    @RequiresPermissions("mall:product:hongbaodelete")
    @Log("删除红包")
    public RJson deleteById(@PathVariable Long id){
        productService.deleteById(id);
        return RJson.ok("红包删除成功");
    }

    @GetMapping("/delete/{id}")
    @ResponseBody
    @RequiresPermissions("mall:product:delete")
    @Log("删除红包")
    public RJson delete(@PathVariable Long id){
        productService.deleteById(id);
        return RJson.ok("红包删除成功");
    }

    /**
     * TODO 保存产品详情信息
     * @param id
     * @param html
     * @return
     */
    @PostMapping("/saveDetail")
    @ResponseBody
    @RequiresPermissions("mall:product:edit")
    @Log("保存产品详情")
    public RJson saveDetail(@RequestParam Long id,
                            @RequestParam String html){
        if(!productService.saveDetail(id, html)){
            logger.error("保存产品详情失败");
            throw new AipenException("保存失败");
        }
        return RJson.ok();
    }

    @PostMapping("/upload")
    @ResponseBody
    public RJson upload(@RequestParam("file") MultipartFile uploadFile){

        if(uploadFile == null){
            logger.error("未获取到文件，上传失败");
            throw new AipenException("上传失败");
        }
        // 文件后缀
        String fileSuffix = uploadFile.getOriginalFilename().substring(uploadFile.getOriginalFilename().lastIndexOf("."));
        String fileName = IdUtil.simpleUUID() + fileSuffix;
        try {
            qiNiuKit.upload(uploadFile.getInputStream(), fileName);
        } catch (IOException e) {
            logger.error("文件上传七牛云空间失败,{}", e);
            throw new AipenException("上传失败");
        }
        return RJson.ok().setData(qiNiuConfig.getDomain() + fileName);
    }
}
