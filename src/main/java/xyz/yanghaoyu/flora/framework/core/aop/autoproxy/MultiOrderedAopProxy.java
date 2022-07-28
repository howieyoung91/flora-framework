/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.framework.core.aop.autoproxy;

import xyz.yanghaoyu.flora.framework.core.aop.interceptor.AdviceChain;
import xyz.yanghaoyu.flora.framework.core.aop.interceptor.AdvicePoint;
import xyz.yanghaoyu.flora.framework.core.aop.interceptor.MethodEnhanceAdviceInterceptor;
import xyz.yanghaoyu.flora.framework.core.aop.interceptor.MultiMethodInterceptor;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/11/14 19:50<i/>
 * @version 1.0
 */


public interface MultiOrderedAopProxy extends AopProxy {
    // 把多个 Point 合并一下
    default MultiMethodInterceptor mergeAndSort(List<MultiMethodInterceptor> interceptors) {
        if (interceptors.size() == 0) {
            return null;
        }
        // 真正使用的拦截器
        MethodEnhanceAdviceInterceptor usingInterceptor = new MethodEnhanceAdviceInterceptor();
        LinkedList<AdvicePoint>        points           = new LinkedList<>();
        AdviceChain                    chain            = new AdviceChain(points);

        // 加入到新的 adviceChain 中
        for (MultiMethodInterceptor interceptor : interceptors) {
            interceptor.getMethodChain().forEach(chain::addPoint);
        }

        // 完成排序
        Collections.sort(points);

        chain.forEach(point -> usingInterceptor.addAdvice((AdvicePoint) point));
        return usingInterceptor;
    }
}
