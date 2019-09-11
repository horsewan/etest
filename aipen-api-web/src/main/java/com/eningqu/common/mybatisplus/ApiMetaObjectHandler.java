package com.eningqu.common.mybatisplus;

import com.baomidou.mybatisplus.mapper.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @desc TODO  mybatis-plus自定义填充公共字段 ,即没有传的字段自动填充
 * @author     Yanghuangping
 * @date       2018/4/17 16:25
 * @version    1.0
 **/
@Component
public class ApiMetaObjectHandler extends MetaObjectHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());

    public final static String CREATE_TIME = "createTime";
    public final static String UPDATE_TIME = "updateTime";


    /**
     * 新增填充
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {

        Object createTime = getFieldValByName(CREATE_TIME, metaObject);
        if (createTime == null) {
            setFieldValByName(CREATE_TIME, new Date(), metaObject);
        }

        Object updateTime = getFieldValByName(UPDATE_TIME, metaObject);
        if (updateTime == null) {
            setFieldValByName(UPDATE_TIME, new Date(), metaObject);
        }
    }

    /**
     * 更新填充
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        Object updateTime = getFieldValByName(UPDATE_TIME, metaObject);
        if (updateTime == null) {
            setFieldValByName(UPDATE_TIME, new Date(), metaObject);
        }
    }
}
