package com.eningqu.modules.api.service.impl;

import com.eningqu.common.base.service.impl.BaseServiceImpl;
import com.eningqu.common.base.vo.RJson;
import com.eningqu.common.exception.ServiceException;
import com.eningqu.common.kit.PasswordKit;
import com.eningqu.common.kit.QRCodeKit;
import com.eningqu.domain.api.BusinessInfo;
import com.eningqu.modules.api.mapper.BusinessMapper;
import com.eningqu.modules.api.service.IBusinessService;
import com.google.zxing.WriterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * @author Yanghuangping
 * @version 1.0
 * @desc TODO
 * @since 2018/11/20 16:43
 **/

@Service
public class BusinessServiceImpl extends BaseServiceImpl<BusinessMapper, BusinessInfo> implements IBusinessService {

    @Autowired
    private QRCodeKit qrCodeKit;

    @Transactional(propagation = Propagation.REQUIRED,
            isolation = Isolation.READ_COMMITTED,
            timeout = 30,
            rollbackFor = RuntimeException.class)
    @Override
    public boolean updateStatus(Long id) throws ServiceException {
        try {
            BusinessInfo bleDevice = baseMapper.selectById(id);
            baseMapper.updateById(bleDevice);
        } catch (RuntimeException e) {
            throw new ServiceException(e);
        }
        return true;
    }

    @Override
    public BusinessInfo queryByWhere(String phone) {
        return baseMapper.queryByWhere(phone);
    }

    @Override
    public BusinessInfo queryByTicket(String ticket) {
        return baseMapper.queryByTicket(ticket);
    }

    @Override
    public boolean updateBusinessById(Long id, String bName, String bPhone, String bPerson, String bAddress, BigDecimal bSolePrice, BigDecimal bPrice) {
        return baseMapper.updateBusinessById(id, bName, bPhone, bPerson, bAddress, bSolePrice, bPrice);
    }

    @Override
    public boolean updateBusinessUnionById(Long id, String macid, String mackey,String mchSta) {
        return baseMapper.updateBusinessUnionById(id, macid, mackey,mchSta);
    }

    @Override
    public boolean updateSignById(Long id, String bSign, String bCdnQr) {
        return baseMapper.updateSignById(id, bSign, bCdnQr);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,
            isolation = Isolation.READ_COMMITTED,
            timeout = 30,
            rollbackFor = Exception.class)
    public RJson insertBusinessInfo(BusinessInfo businessInfo) throws IndexOutOfBoundsException, IOException, WriterException {
        if (!this.insert(businessInfo)) {
            logger.error("商户信息新增失败");
            return RJson.error("商户信息新增失败");
        }

        //TODO 创建商家二维码 已经生成了二维码则直接返回给用户
        if (businessInfo.getbSign() != null && !"".equals(businessInfo.getbSign())) {
            return RJson.ok("商户信息新增成功");
            //return RJson.ok("商户二维码生成成功").setMsg(businessInfo.getbCdnQr());
        }

        //获取个代编号、商家统一社会编码
        String titcket = businessInfo.getbTicket();

        //存入当前商户固定密文，1-3  2-4
        PasswordKit passwordKit = new PasswordKit();
        String ticket = passwordKit.entrypt(businessInfo.getbTicket());//统一社会信用编码不变
        String bId = passwordKit.entrypt(businessInfo.getId() + "");//商户id
        try {
            File file1 = new File("");
            String filePath1 = null;
//        System.out.println(filePath1);
            //TODO 规则  3>ticket>1>bId>2<<singleNo
//        String realPath = "E:"+File.separator+"futruedaoUpload"+File.separator;
            filePath1 = file1.getCanonicalPath() + File.separator + "static\\";
            String context = titcket.substring(8) + ">" + ticket + ">" + titcket.substring(0, 4) + ">" + bId + ">" + titcket.substring(4, 8) + "<<" + businessInfo.getbSingleNo();
            String imageUrl = null;
            //保存该商家二维码数据和标识，只生成一次
            imageUrl = qrCodeKit.getQRCode(filePath1, context);
            this.updateSignById(businessInfo.getId(), context, imageUrl);
        } catch (IOException e) {
            throw e;
        } catch (WriterException e) {
//            return RJson.error("商家二维码生成错误，请重试。");
            throw e;
        } catch (IndexOutOfBoundsException e) {
//            return RJson.error("统一社会信用编码错误，请输入正确编码。");
            throw e;
        }
        return RJson.ok("商户信息新增成功");

    }


}
