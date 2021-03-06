package xyz.yanghaoyu.flora.core.aop.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import xyz.yanghaoyu.flora.core.aop.advice.MethodAroundAdvice;

/**
 * 环绕增强拦截器
 */
@Deprecated
public class MethodAroundAdviceInterceptor implements MethodInterceptor {
    protected MethodAroundAdvice advice;

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        // 触发环绕增强
        return advice.around(
                methodInvocation.getMethod(),
                methodInvocation.getArguments(),
                methodInvocation.getThis()
        );
    }
}
