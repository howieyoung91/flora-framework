package xyz.yanghaoyu.flora.core.aop.interceptor;

import xyz.yanghaoyu.flora.structure.Chain;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/11/12 23:26<i/>
 * @version 1.0
 */
public interface AdvicePoint extends Chain.Point, Comparable<AdvicePoint> {

    default int compareTo(AdvicePoint o) {
        return Integer.compare(getOrder(), o.getOrder());
    }

    default Object proceed(Chain chain) throws Throwable {
        return proceed((AdviceChain) chain);
    }

    int getOrder();

    Object proceed(AdviceChain chain) throws Throwable;
}