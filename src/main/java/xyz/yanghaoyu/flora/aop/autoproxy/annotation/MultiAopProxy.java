package xyz.yanghaoyu.flora.aop.autoproxy.annotation;

import xyz.yanghaoyu.flora.aop.autoproxy.AopProxy;
import xyz.yanghaoyu.flora.aop.interceptor.AdviceChain;
import xyz.yanghaoyu.flora.aop.interceptor.MethodEnhanceAdviceInterceptor;
import xyz.yanghaoyu.flora.aop.interceptor.AdvicePoint;
import xyz.yanghaoyu.flora.aop.interceptor.MultiMethodInterceptor;

import java.util.List;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/11/14 19:50<i/>
 * @version 1.0
 */


public interface MultiAopProxy extends AopProxy {
    // 把多个 Point 合并一下
    default MultiMethodInterceptor merge(List<MultiMethodInterceptor> methodInterceptors) {
        if (methodInterceptors.size() == 0) {
            return null;
        }
        MethodEnhanceAdviceInterceptor res = new MethodEnhanceAdviceInterceptor();
        for (MultiMethodInterceptor methodInterceptor : methodInterceptors) {
            AdviceChain chain = methodInterceptor.getMethodChain();
            chain.forEach(point -> res.addAdvice((AdvicePoint) point));
        }
        return res;
    }
}
