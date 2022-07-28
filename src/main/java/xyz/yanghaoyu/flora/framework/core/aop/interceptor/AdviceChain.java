/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.framework.core.aop.interceptor;

import org.aopalliance.intercept.MethodInvocation;
import xyz.yanghaoyu.flora.framework.structure.Chain;

import java.util.Collection;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/11/12 23:30<i/>
 * @version 1.0
 */


public class AdviceChain extends Chain {
    private ThreadLocal<MethodInvocation> methodInvocationThreadLocal = new ThreadLocal<>();

    public AdviceChain(Collection<AdvicePoint> points) {
        super((Collection) points);
    }

    public MethodInvocation getMethodInvocation() {
        return methodInvocationThreadLocal.get();
    }

    public AdviceChain setMethodInvocation(MethodInvocation methodInvocation) {
        methodInvocationThreadLocal.set(methodInvocation);
        return this;
    }

    @Override
    protected Object doEnd() throws Throwable {
        MethodInvocation methodInvocation = methodInvocationThreadLocal.get();
        // 在执行链的最后调用真实的方法
        Object result = null;
        try {
            // CglibMethodInvocation invocation = (CglibMethodInvocation) methodInvocation;
            // result = invocation.getMethodProxy().invokeSuper(invocation.getProxy(), invocation.getArguments());

            result = methodInvocation.proceed();
            // result = methodInvocation.getMethod().invoke(methodInvocation.getThis(), methodInvocation.getArguments());
        } finally {
            methodInvocationThreadLocal.remove();
        }
        return result;
    }
}
