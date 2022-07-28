/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.framework.core.aop.autoproxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import xyz.yanghaoyu.flora.framework.core.aop.AdvisedSupport;

import java.lang.reflect.Method;

/**
 * cglib 自动代理
 *
 * @author <a href="https://www.yanghaoyu.xyz">Howie Young</a><i>on 2021/8/12 18:10<i/>
 * @version 1.0
 */
@Deprecated
public class Cglib2AopProxy implements AopProxy, MethodInterceptor {
    private final AdvisedSupport advisedSupport;

    public Cglib2AopProxy(AdvisedSupport advised) {
        this.advisedSupport = advised;
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
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        // 方法匹配
        if (advisedSupport.getMethodMatcher().matches(
                method, advisedSupport.getTargetSource().getTarget().getClass())
        ) {
            org.aopalliance.intercept.MethodInterceptor methodInterceptor =
                    advisedSupport.getMethodInterceptor();
            return methodInterceptor.invoke(
                    new CglibMethodInvocation(
                            advisedSupport.getTargetSource().getTarget(), o,
                            method, args, methodProxy
                    ));
        }
        // 不使用增强, 直接调用真实对象的方法
        return methodProxy.invoke(advisedSupport.getTargetSource().getTarget(), args);
    }
}
