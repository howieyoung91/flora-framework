package xyz.yanghaoyu.flora.aop.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import xyz.yanghaoyu.flora.aop.advice.MethodAroundAdvice;

/**
 * 环绕增强拦截器
 */
public class MethodAroundAdviceInterceptor implements MethodInterceptor {
    private MethodAroundAdvice advice;

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        // 触发环绕增强
        return this.advice.around(
                methodInvocation.getMethod(),
                methodInvocation.getArguments(),
                methodInvocation.getThis()
        );
    }
}
