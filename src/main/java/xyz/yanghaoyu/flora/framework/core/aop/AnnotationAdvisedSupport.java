/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.framework.core.aop;

import xyz.yanghaoyu.flora.framework.core.aop.interceptor.MultiMethodInterceptor;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/11/13 18:00<i/>
 * @version 1.0
 */


public class AnnotationAdvisedSupport {
    //  false -> jdk true -> cglib
    private boolean proxyTargetClass = false;
    // 被代理的目标对象
    private TargetSource targetSource;

    private Map<MethodMatcher, MultiMethodInterceptor> map = new HashMap<>();
    Set<Map.Entry<MethodMatcher, MultiMethodInterceptor>> entries = null;

    public boolean isProxyTargetClass() {
        return proxyTargetClass;
    }

    public void setProxyTargetClass(boolean proxyTargetClass) {
        this.proxyTargetClass = proxyTargetClass;
    }

    public TargetSource getTargetSource() {
        return targetSource;
    }

    public void setTargetSource(TargetSource targetSource) {
        this.targetSource = targetSource;
    }

    public void addMethodInterceptor(MethodMatcher methodMatcher, MultiMethodInterceptor methodInterceptor) {
        this.map.put(methodMatcher, methodInterceptor);
    }

    public Set<Map.Entry<MethodMatcher, MultiMethodInterceptor>> getMethodInterceptorSet() {
        if (entries == null) {
            entries = this.map.entrySet();
        }
        return entries;
    }
}
