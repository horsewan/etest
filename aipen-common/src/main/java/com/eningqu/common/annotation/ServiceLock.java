package com.eningqu.common.annotation;

import java.lang.annotation.*;

/**
 *
 * @desc TODO  锁注解
 * @author     Yanghuangping
 * @since      2018/7/13 17:05
 * @version    1.0
 *
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ServiceLock {
}
