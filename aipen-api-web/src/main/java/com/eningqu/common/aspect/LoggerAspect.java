package com.eningqu.common.aspect;

import cn.hutool.json.JSONUtil;
import com.eningqu.common.annotation.Log;
import com.eningqu.common.kit.DateTimeKit;
import com.eningqu.common.kit.IPKit;
import com.eningqu.domain.system.SysLog;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.ibatis.javassist.ClassClassPath;
import org.apache.ibatis.javassist.ClassPool;
import org.apache.ibatis.javassist.CtClass;
import org.apache.ibatis.javassist.CtMethod;
import org.apache.ibatis.javassist.bytecode.CodeAttribute;
import org.apache.ibatis.javassist.bytecode.LocalVariableAttribute;
import org.apache.ibatis.javassist.bytecode.MethodInfo;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.concurrent.Executor;

/**
 *
 * @desc TODO  用户请求URL 日志记录 AOP
 * @author     Yanghuangping
 * @since      2018/7/11 15:29
 * @version    1.0
 *
 **/
@Aspect
@Order(1)
@Component
public class LoggerAspect {

    private Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    @Qualifier("loggerThreadPool")
    private Executor logThreadPool;

    @Pointcut("@annotation(com.eningqu.common.annotation.Log)")
    public void pointCut(){}

    /**
     * TODO 方法执行之前 执行通知
     * @param joinPoint
     * @throws Throwable
     */
    @Before("pointCut()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {}

    /**
     * TODO 方法执行后，只有在方法完成时，才能执行通知
     * @param result
     * @throws Throwable
     */
    @AfterReturning(pointcut = "pointCut()", returning = "result")
    public void doAfterReturning(JoinPoint joinPoint, Object result) throws Throwable {


    }

    /**
     * TODO 方法执行后，只有在方法抛出异常时，才能执行通知
     * @param joinPoint
     * @param ex
     */
    @AfterThrowing(pointcut = "pointCut()", throwing = "ex")
    public void doAfterThrowing(JoinPoint joinPoint, Exception ex){

    }

    /**
     * TODO 使用javassist来获取方法参数名称
     * @param clazz    类名
     * @param method_name   方法名
     * @return
     * @throws Exception
     */
    private String[] getFieldsName(Class<?> clazz, String method_name) throws Exception {
        String clazz_name = clazz.getName();
        ClassPool pool = ClassPool.getDefault();
        ClassClassPath classPath = new ClassClassPath(clazz);
        pool.insertClassPath(classPath);

        CtClass ctClass = pool.get(clazz_name);
        CtMethod ctMethod = ctClass.getDeclaredMethod(method_name);
        MethodInfo methodInfo = ctMethod.getMethodInfo();
        CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
        LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
        if(attr == null){
            return null;
        }
        String[] paramsArgsName = new String[ctMethod.getParameterTypes().length];
        int pos = Modifier.isStatic(ctMethod.getModifiers()) ? 0 : 1;
        for (int i=0;i<paramsArgsName.length;i++){
            paramsArgsName[i] = attr.variableName(i + pos);
        }
        return paramsArgsName;
    }

    /**
     * TODO 打印方法参数值  基本类型直接打印，非基本类型需要重写toString方法
     * @param paramsArgsName    方法参数名数组
     * @param paramsArgsValue   方法参数值数组
     */
    private String getParams(String[] paramsArgsName, Object[] paramsArgsValue){
        if(ArrayUtils.isEmpty(paramsArgsName) || ArrayUtils.isEmpty(paramsArgsValue)){
            logger.info("该方法没有参数");
            return "";
        }
        StringBuffer buffer = new StringBuffer();
        for (int i=0; i<paramsArgsName.length; i++){
            //参数名
            String name = paramsArgsName[i];
            //参数值
            Object value = paramsArgsValue[i];
            buffer.append(name +" = ");
            if(value == null){
                buffer.append("'',");
                continue;
            }
            if(isPrimite(value.getClass())){
                buffer.append(value + "  ,");
            }else {
                buffer.append(JSONUtil.toJsonStr(value) + "  ,");
            }
        }
        return buffer.toString();
    }

    /**
     * TODO 判断是否为基本类型
     * @param clazz clazz
     * @return  true：是;     false：不是
     */
    private boolean isPrimite(Class<?> clazz){
        return
            (
                clazz.equals(String.class) ||
                    clazz.equals(Integer.class)||
                    clazz.equals(Byte.class) ||
                    clazz.equals(Long.class) ||
                    clazz.equals(Double.class) ||
                    clazz.equals(Float.class) ||
                    clazz.equals(Character.class) ||
                    clazz.equals(Short.class) ||
                    clazz.equals(BigDecimal.class) ||
                    clazz.equals(BigInteger.class) ||
                    clazz.equals(Boolean.class) ||
                    clazz.equals(Date.class) ||
                    clazz.equals(DateTime.class) ||
                    clazz.isPrimitive()
            );
    }



    public class LogTask implements Runnable{

        private JoinPoint joinPoint;
        private String result;
        private String exception;
        private String loginName;
        private HttpServletRequest request;
        public LogTask(JoinPoint joinPoint, HttpServletRequest request, String loginName, String result) {
            this(joinPoint, request, loginName, result, "");
        }

        public LogTask(JoinPoint joinPoint, HttpServletRequest request, String loginName, String result, String exception) {
            this.joinPoint = joinPoint;
            this.request = request;
            this.result = result;
            this.exception = exception;
            this.loginName = loginName;
        }

        @Override
        public void run() {

        }
    }

    private SysLog setRequestInfo(JoinPoint joinPoint, HttpServletRequest request){
        Class<?> className = joinPoint.getTarget().getClass();
        String methodName = joinPoint.getSignature().getName();
        String paramsJson = null;
        try {
            paramsJson = getParams(getFieldsName(className, methodName), joinPoint.getArgs());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Class[] argClass = ((MethodSignature)joinPoint.getSignature()).getParameterTypes();
        Method method = null;
        String operation = null;
        try {
            method = className.getMethod(methodName, argClass);
        } catch (NoSuchMethodException e) {
            logger.error("", e);
        }
        if (method.isAnnotationPresent(Log.class)) {
            Log annotation = method.getAnnotation(Log.class);
            operation = annotation.value();
        }
        SysLog log = new SysLog();
        log.setIp(IPKit.getIpAddr(request));
        log.setCreateTime(DateTimeKit.getDate());
        log.setFunc(operation);
        log.setUrl(request.getServletPath());
        // joinPoint.getSignature().getDeclaringTypeName()+"."+joinPoint.getSignature().getName()+"()" 请求方法
        log.setParams(JSONUtil.toJsonStr(paramsJson));
        return log;
    }

}
