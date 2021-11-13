package xyz.yanghaoyu.flora.aop.interceptor;

import xyz.yanghaoyu.flora.structure.Chain;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/11/12 23:26<i/>
 * @version 1.0
 */

public interface MethodPoint extends Chain.Point, Comparable<MethodPoint> {

    default int compareTo(MethodPoint o) {
        return getPriority() - o.getPriority();
    }

    default Object proceed(Chain chain) throws Throwable {
        return proceed((MethodChain) chain);
    }

    int getPriority();

    Object proceed(MethodChain chain) throws Throwable;
}