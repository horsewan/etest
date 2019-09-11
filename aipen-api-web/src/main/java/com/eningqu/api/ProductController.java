package com.eningqu.api;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.eningqu.common.base.vo.RJson;
import com.eningqu.common.enums.ProductStatusEnum;
import com.eningqu.common.enums.StatusEnum;
import com.eningqu.common.exception.AipenException;
import com.eningqu.vo.LoginInfoHelper;
import com.eningqu.service.IProductDetailService;
import com.eningqu.service.IProductService;
import com.eningqu.service.IUserCreditRecordService;
import com.eningqu.domain.api.HongbaoResult;
import com.eningqu.domain.api.Product;
import com.eningqu.domain.api.ProductDetail;
import com.eningqu.domain.api.ProductResult;
import com.eningqu.vo.UserCreditRecordTemp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @desc TODO  产品接口
 * @author Yanghuangping
 * @version 1.0
 * @since 2018/7/5 15:18
 **/
@RestController()
@RequestMapping("/api/product")
public class ProductController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IProductService productService;

    @Autowired
    private IProductDetailService productDetailService;

    @Autowired
    private IUserCreditRecordService userCreditRecordService;

    /**
     * TODO 查询产品列表
     *
     * @param oneClass
     * @return
     */
    @GetMapping("/list")
    public RJson list(@RequestParam String oneClass) {
        Long uId = LoginInfoHelper.getUserID();
        EntityWrapper<Product> wrapper = new EntityWrapper<>();
        wrapper.setSqlSelect("id", "product_no", "product_name", "product_desc", "original_price", "sale_price", "thumb_pic_url","product_detail");
        if(oneClass.equals("1")){
            wrapper.eq("one_class", oneClass);
            wrapper.orderBy("create_time",false);
            List<Product> productList = productService.selectList(wrapper);
            if(productList==null&&productList.size()==0){
                return RJson.ok();
            }
            List<UserCreditRecordTemp> userCreditRecordResultList = userCreditRecordService.selectUserCreditRecordStaById(oneClass,uId);
            List<HongbaoResult> hongbaoResultList = new ArrayList<>();
            for (Product product:productList) {
                HongbaoResult hongbaoResult = new HongbaoResult();
                hongbaoResult.setpId(product.getId());
                hongbaoResult.setProductNo(product.getProductNo());
                hongbaoResult.setProductName(product.getProductName());
                hongbaoResult.setProductDesc(product.getProductDesc());
                hongbaoResult.setHongbaoPrice(product.getOriginalPrice());
                hongbaoResult.setThumbPicUrl(product.getThumbPicUrl());
                if(userCreditRecordResultList!=null&&userCreditRecordResultList.size()>0){
                    boolean bool = false;
                    for (UserCreditRecordTemp userCreditRecordTemp: userCreditRecordResultList) {
                        if(userCreditRecordTemp.getVipHongbao()==product.getId()){
                            bool = true;
                            break;
                        }
                    }
                    hongbaoResult.setHongbaoSta(bool?"Y":"N");
                }else{
                    hongbaoResult.setHongbaoSta("N");
                }
                hongbaoResultList.add(hongbaoResult);
            }
            return RJson.ok().setData(hongbaoResultList);
        }else if(oneClass.equals("2")){
            wrapper.eq("one_class", oneClass).eq("status", ProductStatusEnum.SELLING.getValue());
            wrapper.orderBy("create_time",false);
            List<Product> productList = productService.selectList(wrapper);
            if(productList==null&&productList.size()==0){
                return RJson.ok();
            }
            List<UserCreditRecordTemp> userCreditRecordResultList = userCreditRecordService.selectUserCreditRecordStaById(oneClass,uId);
            List<ProductResult> productResultList = new ArrayList<>();
            for (Product product:productList) {
                ProductResult productResult = new ProductResult();
                productResult.setpId(product.getId());
                productResult.setOriginalPrice(product.getOriginalPrice());
                productResult.setProductDesc(product.getProductDesc());
                productResult.setProductName(product.getProductName());
                productResult.setProductNo(product.getId()+"");
                productResult.setSalePrice(product.getSalePrice());
                productResult.setSoldQuantity(product.getSoldQuantity());
                productResult.setStockQuantity(product.getStockQuantity());
                productResult.setThumbPicUrl(product.getThumbPicUrl());
                productResult.setHongbaoPrice(new BigDecimal(product.getProductDetail()));
                if(userCreditRecordResultList!=null&&userCreditRecordResultList.size()>0){
                    boolean bool = false;
                    for (UserCreditRecordTemp userCreditRecordTemp: userCreditRecordResultList) {
                        if(userCreditRecordTemp.getVipHongbao()==product.getId()){
                            bool = true;
                            break;
                        }
                    }
                    productResult.setHongbaoSta(bool?"Y":"N");
                }else{
                    productResult.setHongbaoSta("N");
                }
                productResultList.add(productResult);
            }
            return RJson.ok().setData(productResultList);
        }else{
            return RJson.error("产品参数不支持");
        }
    }

    /**
     * TODO 根据商品ID查询商品详细
     * @param productId
     * @return
     */
    @GetMapping("/detail/{productId}")
    public RJson detailShow(@PathVariable Long productId){
        EntityWrapper<Product> wrapper = new EntityWrapper<>();
        wrapper.setSqlSelect("id", "product_no", "product_name", "product_desc", "original_price", "sale_price", "thumb_pic_url");
        wrapper.eq("id", productId).eq("status", ProductStatusEnum.SELLING.getValue());
        Product product = productService.selectOne(wrapper);
        if(product == null){
            logger.error("产品[ID = "+productId+"]不存在");
            throw new AipenException("产品不存在");
        }
        EntityWrapper<ProductDetail> detailWrapper = new EntityWrapper<>();
        detailWrapper.setSqlSelect("id", "pid", "show_pic_url");
        detailWrapper.eq("pid", productId);
        detailWrapper.eq("del_status", StatusEnum.YES.getValue());
        List<ProductDetail> detailList = productDetailService.selectList(detailWrapper);
        product.setDetailList(detailList);
        return RJson.ok().setData(product);
    }

}
