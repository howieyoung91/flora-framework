package xyz.yanghaoyu.flora.core.aop.advice;

import jdk.jfr.Description;

import java.lang.reflect.Method;

/**
 * @author <a href="https://www.yanghaoyu.xyz">Howie Young</a><i>on 2021/8/12 18:02<i/>
 * @version 1.0
 */
@FunctionalInterface
@Deprecated
public interface MethodAroundAdvice extends AroundAdvice {
    Object around(Method method, Object[] args, Object target) throws Throwable;
}
