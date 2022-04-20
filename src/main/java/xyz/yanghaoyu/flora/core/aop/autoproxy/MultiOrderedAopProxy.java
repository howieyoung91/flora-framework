package xyz.yanghaoyu.flora.core.aop.autoproxy;

import xyz.yanghaoyu.flora.core.aop.autoproxy.AopProxy;
import xyz.yanghaoyu.flora.core.aop.interceptor.AdviceChain;
import xyz.yanghaoyu.flora.core.aop.interceptor.AdvicePoint;
import xyz.yanghaoyu.flora.core.aop.interceptor.MethodEnhanceAdviceInterceptor;
import xyz.yanghaoyu.flora.core.aop.interceptor.MultiMethodInterceptor;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/11/14 19:50<i/>
 * @version 1.0
 */


public interface MultiOrderedAopProxy extends AopProxy {
    // 把多个 Point 合并一下
    default MultiMethodInterceptor mergeAndSort(List<MultiMethodInterceptor> methodInterceptors) {
        if (methodInterceptors.size() == 0) {
            return null;
        }
        // 真正使用的拦截器
        MethodEnhanceAdviceInterceptor willUsedMethodEnhanceAdviceInterceptor = new MethodEnhanceAdviceInterceptor();
        LinkedList<AdvicePoint> points = new LinkedList<>();
        AdviceChain adviceChain = new AdviceChain(points);
        
        // 加入到新的 adviceChain 中
        for (MultiMethodInterceptor methodInterceptor : methodInterceptors) {
            methodInterceptor.getMethodChain().forEach(adviceChain::addPoint);
        }

        // 完成排序
        Collections.sort(points);

        adviceChain.forEach(point -> willUsedMethodEnhanceAdviceInterceptor.addAdvice((AdvicePoint) point));
        return willUsedMethodEnhanceAdviceInterceptor;
    }
}
