package xyz.yanghaoyu.flora.aop.interceptor;

import xyz.yanghaoyu.flora.structure.Chain;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/11/12 23:26<i/>
 * @version 1.0
 */

public interface AdvicePoint extends Chain.Point, Comparable<AdvicePoint> {

    default int compareTo(AdvicePoint o) {
        return getPriority() - o.getPriority();
    }

    default Object proceed(Chain chain) throws Throwable {
        return proceed((AdviceChain) chain);
    }

    int getPriority();

    Object proceed(AdviceChain chain) throws Throwable;
}