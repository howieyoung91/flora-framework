package xyz.yanghaoyu.flora.core.aop.interceptor;

import org.aopalliance.intercept.MethodInterceptor;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/11/13 13:54<i/>
 * @version 1.0
 */


public interface MultiMethodInterceptor extends MethodInterceptor {
    void addAdvice(AdvicePoint point);

    AdviceChain getMethodChain();
}
