package xyz.yanghaoyu.flora.core.aop.autoproxy;

import xyz.yanghaoyu.flora.core.aop.AnnotationAdvisedSupport;
import xyz.yanghaoyu.flora.core.aop.interceptor.MultiMethodInterceptor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Annotation jdk 自动代理
 *
 * @author <a href="https://www.yanghaoyu.xyz">Howie Young</a><i>on 2021/8/12 18:13<i/>
 * @version 1.0
 */

public class AnnotationJdkDynamicAopProxy extends AnnotationAopProxy implements InvocationHandler {
    public AnnotationJdkDynamicAopProxy(AnnotationAdvisedSupport advised) {
        super(advised);
    }

    @Override
    public Object getProxy() {
        return Proxy.newProxyInstance(
                Thread.currentThread().getContextClassLoader(),
                advisedSupport.getTargetSource().getTargetClass(),
                this
        );
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        MultiMethodInterceptor interceptor = cache.get(method);
        if (interceptor != null) {
            return invokeProxyMethod(proxy, method, args, interceptor);
        }

        interceptor = getEnhanceMethodInterceptor(method);

        if (interceptor == null) {
            return method.invoke(advisedSupport.getTargetSource().getTarget(), args);
        } else {
            cache.put(method, interceptor);
            return invokeProxyMethod(proxy, method, args, interceptor);
        }
    }

    private Object invokeProxyMethod(Object proxy, Method method, Object[] args, MultiMethodInterceptor interceptor) throws Throwable {
        return interceptor.invoke(
                new ReflectiveMethodInvocation(
                        advisedSupport.getTargetSource().getTarget(), proxy,
                        method, args
                ));
    }

}
