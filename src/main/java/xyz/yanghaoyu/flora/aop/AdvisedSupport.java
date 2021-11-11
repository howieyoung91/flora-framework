package xyz.yanghaoyu.flora.aop;

import org.aopalliance.intercept.MethodInterceptor;

import java.util.List;

/**
 * @author <a href="https://www.yanghaoyu.xyz">Howie Young</a><i>on 2021/8/12 18:06<i/>
 * @version 1.0
 */


public class AdvisedSupport {
    //  false -> jdk true -> cglib
    private boolean proxyTargetClass = false;
    // 被代理的目标对象
    private TargetSource targetSource;
    // 方法拦截器
    private MethodInterceptor methodInterceptor;
    // 拦截器链
    private List<MethodInterceptor> methodInterceptors;
    // 方法匹配器(检查目标方法是否符合通知条件)
    private MethodMatcher methodMatcher;

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

    public MethodInterceptor getMethodInterceptor() {
        return methodInterceptor;
    }

    public List<MethodInterceptor> getMethodInterceptors() {
        return methodInterceptors;
    }

    public AdvisedSupport setMethodInterceptors(List<MethodInterceptor> methodInterceptors) {
        this.methodInterceptors = methodInterceptors;
        return this;
    }

    public void setMethodInterceptor(MethodInterceptor methodInterceptor) {
        this.methodInterceptor = methodInterceptor;
    }

    public MethodMatcher getMethodMatcher() {
        return methodMatcher;
    }

    public void setMethodMatcher(MethodMatcher methodMatcher) {
        this.methodMatcher = methodMatcher;
    }
}
