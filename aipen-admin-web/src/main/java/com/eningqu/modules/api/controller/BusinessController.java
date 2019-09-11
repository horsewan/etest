package com.eningqu.modules.api.controller;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.eningqu.common.annotation.Log;
import com.eningqu.common.config.QiNiuConfig;
import com.eningqu.common.kit.DateTimeKit;
import com.eningqu.common.kit.QRCodeKit;
import com.eningqu.common.shiro.ShiroKit;
import com.eningqu.domain.api.BleDevice;
import com.eningqu.modules.system.service.ISysUserService;
import com.eningqu.modules.system.vo.ShiroUser;
import com.eningqu.common.base.vo.DataTable;
import com.eningqu.common.base.vo.RJson;
import com.eningqu.common.constant.SearchParam;
import com.eningqu.common.kit.PasswordKit;
import com.eningqu.common.kit.QiNiuKit;
import com.eningqu.domain.api.BusinessInfo;
import com.eningqu.domain.api.UserInfo;
import com.eningqu.modules.api.service.IBleService;
import com.eningqu.modules.api.service.IBusinessService;
import com.eningqu.modules.api.service.IPkgService;
import com.eningqu.modules.api.service.IUserService;
import com.google.common.collect.Maps;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

/**
 * @author Yanghuangping
 * @version 1.0
 * @desc TODO  商户Controller
 * @since 2018/11/20 16:13
 **/

@Controller
@RequestMapping("/mall/business")
public class BusinessController {

    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private IBusinessService businessService;

    @Autowired
    private IPkgService pkgService;

    @Autowired
    private IBleService bleService;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private IUserService userService;

    @Autowired
    private QiNiuConfig qiNiuConfig;

    @Autowired
    private QiNiuKit qiNiuKit;

    @Autowired
    private QRCodeKit qrCodeKit;

    @GetMapping("")
    @RequiresPermissions("nq:business:list")
    public String list() {
        return "api/business/business_list";
    }

    @GetMapping("/bug")
    @RequiresPermissions("nq:business:listbug")
    public String listBug() {
        return "api/business/business_listbug";
    }

    @GetMapping("/page")
    public String page() {
        return "api/business/business_page";
    }

    @GetMapping("/bugpage")
    public String bugpage() {
        return "api/business/business_bug_page";
    }

    @GetMapping("/createPage")
    public String createPage() {
        return "api/business/business_create_page";
    }

    @GetMapping("/createQR/{id}")
    public String createQRPage(@PathVariable Long id) {
        return "api/business/business_qr_page";
    }

    @GetMapping("/dataTable")
    @ResponseBody
    @RequiresPermissions("nq:business:list")
    @Log("商户信息列表查询")
    public DataTable<BusinessInfo> dataTable(@RequestParam int page,
                                             @RequestParam int limit,
                                             @RequestParam(required = false) String phone) {
        //TODO 获取当前登录用户标识，代理则是代理编号
        ShiroUser shiroUser = null;
        if (ShiroKit.isLogin()) {
            shiroUser = ShiroKit.loginInfo();
        } else {
            return null;
        }
        String loginName = shiroUser.getLoginName();

        Map<String, Object> searchParams = Maps.newHashMap();
        if ("O".equals(shiroUser.getUserType())) {
            searchParams.put(SearchParam.SEARCH_RLIKE + "b_single_no", loginName.toUpperCase().substring(0, 2));
        }
//        Map<String, Object> searchParams = Maps.newHashMap();
//        searchParams.put(SearchParam.SEARCH_RLIKE + "b_name", bName);
        searchParams.put(SearchParam.SEARCH_EQ + "b_phone", phone);//手机号
        searchParams.put(SearchParam.SEARCH_EQ + "del_status", "Y");//是否删除
//        searchParams.put(SearchParam.SEARCH_ISNOTNULL + "mch_id", null);
//        searchParams.put(SearchParam.SEARCH_ISNOTNULL + "mch_key", null);
//        searchParams.put(SearchParam.SEARCH_EQ + "mch_sta", phone);//审核状态

        DataTable<BusinessInfo> dataTable = new DataTable();
        dataTable.setPage(page);
        dataTable.setLimit(limit);
        dataTable.setSearchParams(searchParams);
        businessService.pageSearch(dataTable);

        return dataTable;
    }

    @GetMapping("/dataTableBug")
    @ResponseBody
    @RequiresPermissions("nq:business:listbug")
    @Log("商户信息列表查询")
    public DataTable<BusinessInfo> dataTableBug(@RequestParam int page,
                                                @RequestParam int limit,
                                                @RequestParam(required = false) String phone) {
        //TODO 获取当前登录用户标识，代理则是代理编号
        ShiroUser shiroUser = null;
        if (ShiroKit.isLogin()) {
            shiroUser = ShiroKit.loginInfo();
        } else {
            return null;
        }
        String loginName = shiroUser.getLoginName();

        Map<String, Object> searchParams = Maps.newHashMap();
        if ("O".equals(shiroUser.getUserType())) {
            searchParams.put(SearchParam.SEARCH_RLIKE + "b_single_no", loginName.substring(0, 2));
        }
//        Map<String, Object> searchParams = Maps.newHashMap();
//        searchParams.put(SearchParam.SEARCH_RLIKE + "b_name", bName);
        if (phone != null && !"".equals(phone)) {
            searchParams.put(SearchParam.SEARCH_EQ + "b_phone", phone);//手机号
        }

//        searchParams.put(SearchParam.SEARCH_ISNULL + "mch_id", null);
//        searchParams.put(SearchParam.SEARCH_ISNULL + "mch_key", null);
        searchParams.put(SearchParam.SEARCH_EQ + "mch_sta", "1");
        searchParams.put(SearchParam.SEARCH_EQ + "del_status", "Y");//是否删除
        DataTable<BusinessInfo> dataTable = new DataTable();
        dataTable.setPage(page);
        dataTable.setLimit(limit);
        dataTable.setSearchParams(searchParams);
        businessService.pageSearch(dataTable);

        return dataTable;
    }

    @RequestMapping("/add")
    @ResponseBody
    @RequiresPermissions("nq:business:add")
    @Log("新增商户信息")
    public RJson add(BusinessInfo businessInfo) {

        if (businessInfo == null) {
            return RJson.error("新增商户信息参数错误");
        }
        String tempPhone = businessInfo.getbPhone();
        if (!Validator.isMobile(tempPhone)) {
            logger.error("商户手机号：{}格式错误", tempPhone);
            return RJson.error("商户手机号格式错误");
        }
        businessInfo.setbSingleNo(businessInfo.getbSingleNo().toUpperCase());
        //TODO 判断是否为普通用户
        UserInfo userInfo = userService.getUserInfoByPhone(tempPhone);
        if (userInfo == null) {
            return RJson.error("该用户还不是平台用户，不可授权做商户");
        }
        BusinessInfo tempBusinessInfo = businessService.queryByWhere(tempPhone);
        if (tempBusinessInfo != null) {
            return RJson.error("该用户已是商户不可重复授权商户");
        }

        tempBusinessInfo = businessService.queryByTicket(businessInfo.getbTicket());
        if (tempBusinessInfo != null) {
            return RJson.error("该商户已入驻不可重复授权商户");
        }


        //TODO 判断phone是否已经为其它代理（市级代理、团代、商户）
        BleDevice tempBleDevice = bleService.getBleDeviceByAgentNo(businessInfo.getbSingleNo());
        if (null == tempBleDevice) {
            return RJson.error("客服编号不存在,请重新输入!");
        }
        /*

        SysUser tempSysUser = sysUserService.selectByPhone(tempPhone);
        if(tempSysUser!=null){
            return RJson.error("该用户已是客服总监不可重复授权商户");
        }

        AppInfo tempAppInfo2 = pkgService.getAppInfoBySig(tempPhone);
        if(tempAppInfo2!=null){
            return RJson.error("该用户已是客服经理不可重复授权商户");
        }*/

        //TODO 根据地址获取经纬度 https://restapi.amap.com/v3/geocode/geo?address=北京市朝阳区阜通东大街6号&output=XML&key=<用户的key>

        String str = HttpUtil.get("https://restapi.amap.com/v3/geocode/geo?address=" + businessInfo.getbAddress() + "&output=JSON&key=1a32c0167e8db25ecacb5bec6ccbef74");
        if (str != null) {
            String localtion = null;
            JSONObject jsonobject = (JSONObject) JSONObject.parse(str);
            String info = jsonobject.getString("info");
            if ("OK".equals(info)) {
                JSONArray jsonArray = jsonobject.getJSONArray("geocodes");
                if (jsonArray.size() > 0) {
                    JSONObject jsonObject2 = jsonArray.getJSONObject(0);
                    if (jsonObject2 != null) {
                        localtion = jsonObject2.getString("location");
                    }
                } else {
                    return RJson.error("商户地址信息不存在，请重新录入。");
                }
            }
            if (localtion != null && !"".equals(localtion)) {
                if (localtion.contains(",")) {
                    String[] localArr = localtion.split(",");
                    if (localArr != null && localArr.length > 0) {
                        businessInfo.setAddressX(localArr[0]);
                        businessInfo.setAddressY(localArr[1]);
                    }
                }
            }
        } else {
            return RJson.error("商户地址信息不存在，请重新录入。");
        }

        try {
            businessService.insertBusinessInfo(businessInfo);
        } catch (IOException e) {
            e.printStackTrace();
            return RJson.error("操作失败。");
        } catch (WriterException e) {
            e.printStackTrace();
            return RJson.error("商家二维码生成错误，请重试。");
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            return RJson.error("统一社会信用编码错误，请输入正确编码。");
        }
        return RJson.ok("操作成功，后台审核中...");
    }

    @RequestMapping("/edit")
    @ResponseBody
    @RequiresPermissions("nq:business:edit")
    @Log("修改商户信息")
    public RJson edit(BusinessInfo businessInfo) {

        String tempPhone = businessInfo.getbPhone();
        if (tempPhone != null && !"".equals(tempPhone)) {
            if (!Validator.isMobile(businessInfo.getbPhone())) {
                logger.error("mac地址：{}格式错误", tempPhone);
                return RJson.error("商户手机号格式错误");
            }

            //TODO 判断是否为普通用户
            UserInfo userInfo = userService.getUserInfoByPhone(tempPhone);
            if (userInfo == null) {
                return RJson.error("该用户还不是平台用户，不可授权做商户");
            }

            BusinessInfo tempBusinessInfo = businessService.queryByWhere(tempPhone);
            if (tempBusinessInfo != null) {
                if (!tempBusinessInfo.getbTicket().equals(businessInfo.getbTicket())) {
                    return RJson.error(tempPhone + "_该用户已是商户不可重复授权商户");
                }
            }
            //TODO 根据地址获取经纬度 https://restapi.amap.com/v3/geocode/geo?address=北京市朝阳区阜通东大街6号&output=XML&key=<用户的key>

            String str = HttpUtil.get("https://restapi.amap.com/v3/geocode/geo?address=" + businessInfo.getbAddress() + "&output=JSON&key=1a32c0167e8db25ecacb5bec6ccbef74");
            if (str != null) {
                String localtion = null;
                JSONObject jsonobject = (JSONObject) JSONObject.parse(str);
                String info = jsonobject.getString("info");
                if ("OK".equals(info)) {
                    JSONArray jsonArray = jsonobject.getJSONArray("geocodes");
                    if (jsonArray.size() > 0) {
                        JSONObject jsonObject2 = jsonArray.getJSONObject(0);
                        if (jsonObject2 != null) {
                            localtion = jsonObject2.getString("location");
                        }
                    } else {
                        return RJson.error("商户地址信息不存在，请重新录入。");
                    }
                }
                if (localtion != null && !"".equals(localtion)) {
                    if (localtion.contains(",")) {
                        String[] localArr = localtion.split(",");
                        if (localArr != null && localArr.length > 0) {
                            businessInfo.setAddressX(localArr[0]);
                            businessInfo.setAddressY(localArr[1]);
                        }
                    }
                }
            } else {
                return RJson.error("商户地址信息不存在，请重新录入。");
            }

            businessInfo.setMchSta("1");
           /* //TODO 判断phone是否已经为其它代理（市级代理、团代、商户）
            BleDevice tempBleDevice = bleService.getBleDeviceByPhone(tempPhone);
            if(tempBleDevice!=null){
                return RJson.error("该用户已是客服不可重复授权商户");
            }

            SysUser tempSysUser = sysUserService.selectByPhone(tempPhone);
            if(tempSysUser!=null){
                return RJson.error("该用户已是客服总监不可重复授权商户");
            }

            AppInfo tempAppInfo2 = pkgService.getAppInfoBySig(tempPhone);
            if(tempAppInfo2!=null){
                return RJson.error("该用户已是客服经理不可重复授权商户");
            }*/
        }
        businessService.updateById(businessInfo);
//        businessService.updateBusinessById(businessInfo.getId(), businessInfo.getbName(), businessInfo.getbPhone(),
//                businessInfo.getbPerson(), businessInfo.getbAddress(), businessInfo.getbSolePrice(),
//                businessInfo.getbPrice());

        return RJson.ok("商户信息修改成功");
    }

    @RequestMapping("/editbug")
    @ResponseBody
    @RequiresPermissions("nq:business:editbug")
    @Log("修改商户信息")
    public RJson editBug(BusinessInfo businessInfo) {

        String macId = businessInfo.getMchId();
        String macKey = businessInfo.getMchKey();
        if (macId == null && "".equalsIgnoreCase(macId))
            return RJson.error("银联授权商户号参数错误");
        if (macKey == null && "".equalsIgnoreCase(macKey))
            return RJson.error("银联授权商户密文参数错误");
        if ("Y".equals(businessInfo.getMchSta()))
            businessInfo.setMchSta("2");
        else
            businessInfo.setMchSta("3");
        businessService.updateBusinessUnionById(businessInfo.getId(), businessInfo.getMchId(), businessInfo.getMchKey(), businessInfo.getMchSta());

        return RJson.ok("商户信息修改成功");
    }

    @GetMapping("/del/{ids}")
    @ResponseBody
    @RequiresPermissions("nq:business:delete")
    @Log("删除商户信息")
    public RJson delete(@PathVariable List<Long> ids) {
        BusinessInfo businessInfo = new BusinessInfo();
        businessInfo.setDelStatus("N");
        for (Long id : ids) {
            businessInfo.setId(id);
            businessService.updateById(businessInfo);
        }
        return RJson.ok("商户删除成功");
    }

    /**
     * TODO 更改设备使用状态
     *
     * @param id
     * @return
     */
    @GetMapping("/status/{id}")
    @ResponseBody
    @RequiresPermissions("nq:business:status")
    @Log("更新商户状态")
    public RJson status(@PathVariable("id") Long id) {

        if (!businessService.updateStatus(id)) {
            logger.error("商户状态更新失败");
            return RJson.error("商户状态更新失败");
        }
        return RJson.ok("商户状态更新成功");
    }

    @RequestMapping("/downloadBusinessQR")
    @ResponseBody
    @RequiresPermissions("nq:business:download")
    @Log("下载商户二维码信息")
    public RJson downloadBusinessQR(BusinessInfo businessInfo) {

        if (businessInfo == null)
            return RJson.error("商家二维码数据错误");

        //TODO 存在则不可再次生成（会导致变更平台私钥），直接返回已存在临时二维码密文即可。

        //已经生成了二维码则直接返回给用户
        if (businessInfo.getbSign() != null && !"".equals(businessInfo.getbSign())) {
            return RJson.ok("商户二维码生成成功").setMsg(businessInfo.getbCdnQr());
        }

        //获取个代编号、商家统一社会编码
        String titcket = businessInfo.getbTicket();

        //存入当前商户固定密文，1-3  2-4
        PasswordKit passwordKit = new PasswordKit();
        String ticket = passwordKit.entrypt(businessInfo.getbTicket());//统一社会信用编码不变
        String bId = passwordKit.entrypt(businessInfo.getId() + "");//商户id

        File file1 = new File("");
        String filePath1 = null;
        try {
            filePath1 = file1.getCanonicalPath() + File.separator + "static\\";
        } catch (IOException e) {
            e.printStackTrace();
        }
        //规则  3>ticket>1>bId>2<<singleNo
//        String realPath = "E:"+File.separator+"futruedaoUpload"+File.separator;
        String context = titcket.substring(8) + ">" + ticket + ">" + titcket.substring(0, 4) + ">" + bId + ">" + titcket.substring(4, 8) + "<<" + businessInfo.getbSingleNo();
        String imageUrl = null;
        try {
            imageUrl = qrCodeKit.getQRCode(filePath1, context);
        } catch (IOException e) {
            return RJson.error("商家二维码生成错误，请重试。");
        } catch (WriterException e) {
            return RJson.error("商家二维码生成错误，请重试。");
        } finally {
            //保存该商家二维码数据和标识，只生成一次
            businessService.updateSignById(businessInfo.getId(), context, imageUrl);
        }
        return RJson.ok("商户二维码生成成功").setMsg(imageUrl);
    }

    /**
     * TODO 下载文件
     */
    @RequestMapping("/downLoadFile")
    public void downLoadFile(HttpServletRequest request, HttpServletResponse response) {

        /*String url = "http://image.futruedao.com/166a183dbcbe4e86b3b8bfe896d2859a.png";//request.getParameter("path");
        File fileurl = new File(url);
        //浏览器下载后的文件名称showValue,从url中截取到源文件名称以及，以及文件类型，如board.docx;
        String showValue = "png";
        System.out.println(showValue);
        try{
            //根据条件得到文件路径
            System.out.println("===========文件路径==========="+fileurl);
            //将文件读入文件流
            InputStream inStream = new FileInputStream(fileurl);
            //获得浏览器代理信息
            final String userAgent = request.getHeader("USER-AGENT");
            //判断浏览器代理并分别设置响应给浏览器的编码格式
            String finalFileName = null;
            if(StringUtils.contains(userAgent, "MSIE")|| StringUtils.contains(userAgent,"Trident")){//IE浏览器
                finalFileName = URLEncoder.encode(showValue,"UTF8");
                System.out.println("IE浏览器");
            }else if(StringUtils.contains(userAgent, "Mozilla")){//google,火狐浏览器
                finalFileName = new String(showValue.getBytes(), "ISO8859-1");
            }else{
                finalFileName = URLEncoder.encode(showValue,"UTF8");//其他浏览器
            }
            //设置HTTP响应头
            response.reset();//重置 响应头
            response.setContentType("application/x-download");//告知浏览器下载文件，而不是直接打开，浏览器默认为打开
            response.addHeader("Content-Disposition" ,"attachment;filename=\"" +finalFileName+ "\"");//下载文件的名称

            // 循环取出流中的数据
            byte[] b = new byte[1024];
            int len;
            while ((len = inStream.read(b)) > 0){
                response.getOutputStream().write(b, 0, len);
            }
            inStream.close();
            response.getOutputStream().close();
        }catch(Exception e) {
            e.printStackTrace();
        }*/

    }


}
