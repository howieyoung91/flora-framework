package xyz.yanghaoyu.flora.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

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
        return methodInvocation.proceed();
    }
}
