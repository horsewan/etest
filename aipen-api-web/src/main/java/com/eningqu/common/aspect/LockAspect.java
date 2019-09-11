package com.eningqu.common.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @desc TODO
 * @author Yanghuangping
 * @version 1.0
 * @since 2018/7/13 17:08
 **/
@Component
@Scope
@Aspect
@Order(2)
public class LockAspect {

    //互斥锁 参数默认false，不公平锁
    private static Lock lock = new ReentrantLock(true);

    @Pointcut("@annotation(com.eningqu.common.annotation.ServiceLock)")
    public void lockAspect() {

    }

    @Around("lockAspect()")
    public Object around(ProceedingJoinPoint joinPoint) {
        lock.lock();
        Object obj = null;
        try {
            obj = joinPoint.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
        } finally{
            lock.unlock();
        }
        return obj;
    }
}
