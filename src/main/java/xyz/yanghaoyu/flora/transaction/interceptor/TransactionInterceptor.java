/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */
package xyz.yanghaoyu.flora.transaction.interceptor;

import xyz.yanghaoyu.flora.core.Ordered;
import xyz.yanghaoyu.flora.core.aop.autoproxy.CglibMethodInvocation;
import xyz.yanghaoyu.flora.core.aop.interceptor.AdviceChain;
import xyz.yanghaoyu.flora.core.aop.interceptor.AdvicePoint;

public class TransactionInterceptor extends TransactionAspectSupport implements AdvicePoint {
    public static final int ORDER = Ordered.HIGHEST_PRECEDENCE / 2;

    @Override
    public Object proceed(AdviceChain chain) throws Throwable {
        CglibMethodInvocation invocation = (CglibMethodInvocation) chain.getMethodInvocation();
        return invokeWithinTransaction(invocation.getMethod(), invocation.getMethodProxy(), invocation.getThis().getClass(), chain::proceed);
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
