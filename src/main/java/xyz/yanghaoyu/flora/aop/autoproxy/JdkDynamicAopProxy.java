package xyz.yanghaoyu.flora.aop.autoproxy;

import org.aopalliance.intercept.MethodInterceptor;
import xyz.yanghaoyu.flora.aop.AdvisedSupport;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author <a href="https://www.yanghaoyu.xyz">Howie Young</a><i>on 2021/8/12 18:13<i/>
 * @version 1.0
 */


public class JdkDynamicAopProxy implements AopProxy, InvocationHandler {

    private final AdvisedSupport advisedSupport;

    public JdkDynamicAopProxy(AdvisedSupport advised) {
        this.advisedSupport = advised;
    }

    @Override
    public Object getProxy() {
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), advisedSupport.getTargetSource().getTargetClass(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //如果方法匹配的话
        if (advisedSupport.getMethodMatcher().matches(method, advisedSupport.getTargetSource().getTarget().getClass())) {
            MethodInterceptor methodInterceptor = advisedSupport.getMethodInterceptor();
            // 触发增强
            return methodInterceptor.invoke(
                    new ReflectiveMethodInvocation(
                            advisedSupport.getTargetSource().getTarget(), method, args
                    )
            );
        }
        return method.invoke(advisedSupport.getTargetSource().getTarget(), args);
    }


}
