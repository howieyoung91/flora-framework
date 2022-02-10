package xyz.yanghaoyu.flora.core.aop.interceptor;

import org.aopalliance.intercept.MethodInvocation;

import java.util.LinkedList;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/11/12 19:22<i/>
 * @version 1.0
 */


public class MethodEnhanceAdviceInterceptor implements MultiMethodInterceptor {
    private final AdviceChain chain = new AdviceChain(new LinkedList<>());

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        chain.setMethodInvocation(methodInvocation);
        return chain.proceed();
    }

    @Override
    public void addAdvice(AdvicePoint point) {
        chain.addPoint(point);
    }

    @Override
    public AdviceChain getMethodChain() {
        return chain;
    }
}
