/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.framework.core.aop.interceptor;

import xyz.yanghaoyu.flora.framework.structure.Chain;

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

    Object proceed(AdviceChain chain) throws Throwable;

    int getOrder();
}