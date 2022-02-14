package xyz.yanghaoyu.flora.core.aop;

import xyz.yanghaoyu.flora.core.aop.autoproxy.AnnotationCglib2AopProxy;
import xyz.yanghaoyu.flora.core.aop.autoproxy.AopProxy;
import xyz.yanghaoyu.flora.core.aop.autoproxy.AnnotationJdkDynamicAopProxy;

/**
 * 代理工厂 用于生成 代理Bean
 */
public class AnnotationProxyFactory {
    private AnnotationAdvisedSupport advisedSupport;

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
