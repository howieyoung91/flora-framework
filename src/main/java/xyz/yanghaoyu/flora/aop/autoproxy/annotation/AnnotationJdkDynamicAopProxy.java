package xyz.yanghaoyu.flora.aop.autoproxy.annotation;

import xyz.yanghaoyu.flora.aop.AnnotationAdvisedSupport;
import xyz.yanghaoyu.flora.aop.MethodMatcher;
import xyz.yanghaoyu.flora.aop.autoproxy.ReflectiveMethodInvocation;
import xyz.yanghaoyu.flora.aop.interceptor.MultiMethodInterceptor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Annotation jdk 自动代理
 *
 * @author <a href="https://www.yanghaoyu.xyz">Howie Young</a><i>on 2021/8/12 18:13<i/>
 * @version 1.0
 */

public class AnnotationJdkDynamicAopProxy implements MultiAopProxy, InvocationHandler {

    private final AnnotationAdvisedSupport advisedSupport;
    private final Map<Method, MultiMethodInterceptor> cache = new HashMap<>();

    public AnnotationJdkDynamicAopProxy(AnnotationAdvisedSupport advised) {
        this.advisedSupport = advised;
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
            return invokeProxyMethod(method, args, interceptor);
        }

        interceptor = getRealMethodInterceptor(method);

        if (interceptor == null) {
            return method.invoke(advisedSupport.getTargetSource().getTarget(), args);
        } else {
            cache.put(method, interceptor);
            return invokeProxyMethod(method, args, interceptor);
        }
    }

    private MultiMethodInterceptor getRealMethodInterceptor(Method method) {
        MultiMethodInterceptor interceptor;
        ArrayList<MultiMethodInterceptor> temp = findMatchedMethodInterceptor(method);

        interceptor = merge(temp);
        return interceptor;
    }

    private ArrayList<MultiMethodInterceptor> findMatchedMethodInterceptor(Method method) {
        Set<Map.Entry<MethodMatcher, MultiMethodInterceptor>> set = advisedSupport.getMethodInterceptorSet();

        ArrayList<MultiMethodInterceptor> temp = new ArrayList<>();
        for (Map.Entry<MethodMatcher, MultiMethodInterceptor> entry : set) {
            MethodMatcher methodMatcher = entry.getKey();
            MultiMethodInterceptor multiMethodInterceptor = entry.getValue();
            if (methodMatcher.matches(method, advisedSupport.getTargetSource().getTarget().getClass())) {
                temp.add(multiMethodInterceptor);
            }
        }
        return temp;
    }

    private Object invokeProxyMethod(Method method, Object[] args, MultiMethodInterceptor interceptor) throws Throwable {
        return interceptor.invoke(
                new ReflectiveMethodInvocation(
                        advisedSupport.getTargetSource().getTarget(),
                        method, args
                ));
    }

}
