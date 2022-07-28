/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.framework.core.aop.autoproxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import xyz.yanghaoyu.flora.framework.core.aop.AnnotationAdvisedSupport;
import xyz.yanghaoyu.flora.framework.core.aop.interceptor.MultiMethodInterceptor;

import java.lang.reflect.Method;

/**
 * Annotation cglib 自动代理
 *
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/11/14 <i/>
 * @version 1.0
 */

public class AnnotationCglib2AopProxy extends AnnotationAopProxy implements MethodInterceptor {

    public AnnotationCglib2AopProxy(AnnotationAdvisedSupport advised) {
        super(advised);
    }

    @Override
    public Object getProxy() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(advisedSupport.getTargetSource().getTarget().getClass());
        enhancer.setInterfaces(advisedSupport.getTargetSource().getTargetClass());
        enhancer.setCallback(this);
        return enhancer.create();
    }

    @Override
    public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        MultiMethodInterceptor interceptor = cache.get(method);
        if (interceptor != null) {
            return invokeProxyMethod(proxy, method, args, methodProxy, interceptor);
        }

        // 获取真实的拦截器 主要目的是把多个拦截器合并为一个 确保优先级正确
        interceptor = getEnhanceMethodInterceptor(method);

        if (interceptor == null) {
            return invokeActualMethod(args, methodProxy);
        } else {
            cache.put(method, interceptor);
            return invokeProxyMethod(proxy, method, args, methodProxy, interceptor);
        }
    }

    private Object invokeProxyMethod(Object proxy, Method method, Object[] args, MethodProxy methodProxy, MultiMethodInterceptor interceptor) throws Throwable {
        return interceptor.invoke(
                new CglibMethodInvocation(
                        advisedSupport.getTargetSource().getTarget(), proxy,
                        method, args, methodProxy
                ));
    }

    private Object invokeActualMethod(Object[] args, MethodProxy methodProxy) throws Throwable {
        return methodProxy.invoke(advisedSupport.getTargetSource().getTarget(), args);
    }
}
