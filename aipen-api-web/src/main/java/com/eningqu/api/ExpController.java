package com.eningqu.api;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.eningqu.common.base.vo.RJson;
import com.eningqu.common.constant.Global;
import com.eningqu.common.enums.OrderStatusEnum;
import com.eningqu.common.enums.StatusEnum;
import com.eningqu.common.exception.AipenException;
import com.eningqu.common.exp.ExpConfig;
import com.eningqu.common.exp.ExpSetting;
import com.eningqu.domain.api.ExpInfo;
import com.eningqu.domain.api.Orders;
import com.eningqu.service.IExpInfoService;
import com.eningqu.service.IOrderService;
import com.eningqu.common.kit.WebKit;
import com.eningqu.vo.LoginInfo;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 *
 * @desc TODO  快递地址接口
 * @author     Yanghuangping
 * @since      2018/7/7 11:01
 * @version    1.0
 *
 **/
@RestController
@RequestMapping("/api/exp")
public class ExpController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IExpInfoService expInfoService;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private ExpConfig expConfig;
    /**
     * TODO 查询用户收件地址列表
     * @return
     */
    @GetMapping("/list")
    public RJson list(){
        LoginInfo loginInfo = (LoginInfo) WebKit.getRequest().getAttribute(Global.API_LOGIN_INFO);
        EntityWrapper<ExpInfo> wrapper = new EntityWrapper<>();
        wrapper.setSqlSelect("id", "sign_name", "sign_phone", "address");
        wrapper.eq("uid", loginInfo.getId()).eq("del_status", StatusEnum.YES.getValue());
        List<ExpInfo> lists = expInfoService.selectList(wrapper);
        return RJson.ok().setData(lists);
    }

    /**
     * TODO 添加收件地址
     * @param expInfo
     * @return
     */
    @PostMapping("/add")
    public RJson add(ExpInfo expInfo){
        LoginInfo loginInfo = (LoginInfo) WebKit.getRequest().getAttribute(Global.API_LOGIN_INFO);
        expInfo.setUid(loginInfo.getId());
        try {
            expInfoService.insert(expInfo);
        } catch (RuntimeException e) {
            logger.error("收件地址添加异常", e);
            throw new AipenException("添加失败");
        }
        return RJson.ok();
    }

    /**
     * TODO 更新收件地址
     * @param expInfo
     * @return
     */
    @PostMapping("/update")
    public RJson update(ExpInfo expInfo){
        LoginInfo loginInfo = (LoginInfo) WebKit.getRequest().getAttribute(Global.API_LOGIN_INFO);
        expInfo.setUid(loginInfo.getId());
        try {
            expInfoService.updateById(expInfo);
        } catch (RuntimeException e) {
            logger.error("收件地址更新异常", e);
            throw new AipenException("更新失败");
        }
        return RJson.ok();
    }

    /**
     * TODO 删除收件地址
     * @param expId
     * @return
     */
    @GetMapping("/del/{expId}")
    public RJson delete(@PathVariable Long expId){
        LoginInfo loginInfo = (LoginInfo) WebKit.getRequest().getAttribute(Global.API_LOGIN_INFO);
        EntityWrapper<ExpInfo> wrapper = new EntityWrapper<>();
        wrapper.eq("id", expId).eq("uid", loginInfo.getId());
        ExpInfo expInfo = expInfoService.selectOne(wrapper);
        if(expInfo == null){
            logger.error("收件地址不存在");
            throw new AipenException("删除失败");
        }
        expInfo.setDelStatus(StatusEnum.NO.getValue());
        try {
            expInfoService.updateById(expInfo);
        } catch (Exception e) {
            logger.error("收件地址删除异常", e);
            throw new AipenException("删除成功");
        }

        return RJson.ok();
    }

    /**
     * TODO 根据订单号查询物流信息
     * @param orderNumber
     * @return
     */
    @GetMapping("/query/{orderNumber}")
    public RJson queryExp(@PathVariable String orderNumber){

        LoginInfo loginInfo = (LoginInfo) WebKit.getRequest().getAttribute(Global.API_LOGIN_INFO);

        Orders orders = orderService.queryOrderByUid(orderNumber, loginInfo.getId());
        if(orders == null){
            logger.error("未查询到该订单号["+orderNumber+"]的订单记录");
            throw new AipenException("订单不存在");
        }

        // 若订单状态不是【已发货】状态，不能查询物流
        if(!StrUtil.equalsIgnoreCase(OrderStatusEnum.SHIPPED.getValue(), orders.getOrderStatus())){
            logger.error("该订单号["+orderNumber+"]记录不是[已发货]的状态，不能查询物流");
            throw new AipenException("查询物流失败");
        }

        // 若没有快递单号 和 快递编码
        if(StrUtil.isBlank(orders.getExpNo()) || StrUtil.isBlank(orders.getExpCode())){
            logger.error("该订单号["+orderNumber+"]没有快递单号和快递编码");
        }

        String RequestData = "{'OrderCode':'','ShipperCode':'" + orders.getExpCode() + "','LogisticCode':'" + orders.getExpNo() + "'}";

        Map<String, Object> params = Maps.newHashMap();
        params.put("RequestData", URLUtil.encode(RequestData, "UTF-8"));
        params.put("EBusinessID", expConfig.getAppId());
        params.put("RequestType", "1002");
        String dataSign = URLUtil.encode(Base64.encode(DigestUtil.md5Hex(RequestData + expConfig.getAppKey(), "UTF-8"), "UTF-8"), "UTF-8");
        params.put("DataSign", dataSign);
        params.put("DataType", "2");

        String result = HttpUtil.post(ExpSetting.LOGISTIC_QUERY, params);
        JSONObject object = JSONUtil.parseObj(result);
        if(!object.getBool("Success")){
            logger.error("订单号["+orderNumber+"]物流信息查询异常，{}", object.getStr("Reason"));
            throw new AipenException("物流信息查询失败");
        }
        return RJson.ok("物流信息查询成功").setData(object.getJSONArray("Traces"));
    }
}
