package com.eningqu.common.aspect;

import com.eningqu.common.annotation.DataSource;
import com.eningqu.common.db.DataSourceKit;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import java.lang.reflect.Method;

/**
 *
 * @desc TODO  多数据源 AOP
 * @author     Yanghuangping
 * @date       2018/4/28 11:55
 * @version    1.0
 *
 **/
@Aspect
@Order(1)
@Component
public class DataSourceAspect {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Pointcut("@annotation(com.eningqu.common.annotation.DataSource)")
    public void pointCut(){}

    /**
     * TODO 方法执行之前 执行通知
     * @param point
     * @throws Throwable
     */
    @Before("pointCut()")
    public void doBefore(JoinPoint point) throws Throwable {

        String dataSource = DataSourceKit.MASTER;
        //获得当前访问的class
        Class<?> className = point.getTarget().getClass();
        //获得访问的方法名
        String methodName = point.getSignature().getName();
        //得到方法的参数的类型
        Class[] argClass = ((MethodSignature)point.getSignature()).getParameterTypes();
        try {
            // 得到访问的方法对象
            Method method = className.getMethod(methodName, argClass);
            if (method.isAnnotationPresent(DataSource.class)) {
                DataSource annotation = method.getAnnotation(DataSource.class);
                dataSource = annotation.value();
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("", e);
        }
        DataSourceKit.setDataSource(dataSource);
    }

    /**
     * TODO 方法执行后，只有在方法完成时，才能执行通知
     * @param ret
     * @throws Throwable
     */
    @AfterReturning(returning = "ret", pointcut = "pointCut()")
    public void doAfterReturning(Object ret) throws Throwable {
        DataSourceKit.clearDataSource();
    }

    /**
     * TODO 方法执行后，只有在方法抛出异常时，才能执行通知
     * @param joinPoint
     * @param ex
     */
    @AfterThrowing(pointcut = "pointCut()", throwing = "ex")
    public void doAfterThrowing(JoinPoint joinPoint, Exception ex){
        DataSourceKit.clearDataSource();
    }
}
