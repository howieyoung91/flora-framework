/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.framework.core.aop;

import xyz.yanghaoyu.flora.framework.core.aop.autoproxy.AnnotationCglib2AopProxy;
import xyz.yanghaoyu.flora.framework.core.aop.autoproxy.AopProxy;
import xyz.yanghaoyu.flora.framework.core.aop.autoproxy.AnnotationJdkDynamicAopProxy;

/**
 * 代理工厂 用于生成 代理Bean
 */
public class AnnotationProxyFactory {
    private final AnnotationAdvisedSupport advisedSupport;

    public AnnotationProxyFactory(AnnotationAdvisedSupport advisedSupport) {
        this.advisedSupport = advisedSupport;
    }

    public Object getProxy() {
        return createAopProxy().getProxy();
    }

    private AopProxy createAopProxy() {
        if (advisedSupport.isProxyTargetClass()) {
            return new AnnotationCglib2AopProxy(advisedSupport);
        }

        return new AnnotationJdkDynamicAopProxy(advisedSupport);
    }
}
