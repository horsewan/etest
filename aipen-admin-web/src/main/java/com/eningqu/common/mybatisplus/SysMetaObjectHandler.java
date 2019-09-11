package com.eningqu.common.mybatisplus;

import com.baomidou.mybatisplus.mapper.MetaObjectHandler;
import com.eningqu.common.shiro.ShiroKit;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @desc TODO  mybatis-plus自定义填充公共字段 ,即没有传的字段自动填充
 * @author     Yanghuangping
 * @date       2018/4/17 16:25
 * @version    1.0
 **/
@Component
public class SysMetaObjectHandler extends MetaObjectHandler {

    public final static String CREATE_ID = "createId";
    public final static String CREATE_TIME = "createTime";
    public final static String UPDATE_ID = "updateId";
    public final static String UPDATE_TIME = "updateTime";

    /**
     * 新增填充
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {

        String[] names = metaObject.getSetterNames();

        if(hasExsit(names, CREATE_ID)){
            Object createId = getFieldValByName(CREATE_ID, metaObject);
            if (createId == null) {
                setFieldValByName(CREATE_ID, ShiroKit.id(), metaObject);
            }
        }

        Object createTime = getFieldValByName(CREATE_TIME, metaObject);
        if (createTime == null) {
            setFieldValByName(CREATE_TIME, new Date(), metaObject);
        }


    }

    /**
     * 更新填充
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {

        String[] names = metaObject.getSetterNames();

        if(hasExsit(names, UPDATE_ID)){
            Object updateId = getFieldValByName(UPDATE_ID, metaObject);
            if (updateId == null) {
                setFieldValByName(UPDATE_ID, ShiroKit.id(), metaObject);
            }
        }

        Object updateTime = getFieldValByName(UPDATE_TIME, metaObject);
        if (updateTime == null) {
            setFieldValByName(UPDATE_TIME, new Date(), metaObject);
        }
    }


    private boolean hasExsit(String[] names, String fieldName){
        for (String name : names) {
            if(name.equalsIgnoreCase(fieldName)){
                return true;
            }
        }
        return false;
    }
}
