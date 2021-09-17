package xyz.yanghaoyu.flora.aop;

import xyz.yanghaoyu.flora.aop.autoproxy.AopProxy;
import xyz.yanghaoyu.flora.aop.autoproxy.Cglib2AopProxy;
import xyz.yanghaoyu.flora.aop.autoproxy.JdkDynamicAopProxy;

/**
 * 代理工厂 用于生成 代理Bean
 */
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
