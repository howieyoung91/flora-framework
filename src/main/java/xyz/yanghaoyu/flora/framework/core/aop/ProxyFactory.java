/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.framework.core.aop;

import xyz.yanghaoyu.flora.framework.core.aop.autoproxy.AopProxy;
import xyz.yanghaoyu.flora.framework.core.aop.autoproxy.Cglib2AopProxy;
import xyz.yanghaoyu.flora.framework.core.aop.autoproxy.JdkDynamicAopProxy;

/**
 * 代理工厂 用于生成 代理Bean
 */
@Deprecated
public class ProxyFactory {
    private AdvisedSupport advisedSupport;

    public ProxyFactory(AdvisedSupport advisedSupport) {
        this.advisedSupport = advisedSupport;
    }

    public Object getProxy() {
        return createAopProxy().getProxy();
    }

    private AopProxy createAopProxy() {
        if (advisedSupport.isProxyTargetClass()) {
            return new Cglib2AopProxy(advisedSupport);
        }

        return new JdkDynamicAopProxy(advisedSupport);
    }
}
