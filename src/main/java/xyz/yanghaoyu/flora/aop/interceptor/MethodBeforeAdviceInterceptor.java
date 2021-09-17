package xyz.yanghaoyu.flora.aop.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import xyz.yanghaoyu.flora.aop.advice.MethodBeforeAdvice;

/**
 * 前置增强拦截器
 */
public class MethodBeforeAdviceInterceptor implements MethodInterceptor {
    private MethodBeforeAdvice advice;

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        // 触发前置增强
        this.advice.before(
                methodInvocation.getMethod(),
                methodInvocation.getArguments(),
                methodInvocation.getThis()
        );
        // 触发真实的方法
        return methodInvocation.proceed();
    }
}
