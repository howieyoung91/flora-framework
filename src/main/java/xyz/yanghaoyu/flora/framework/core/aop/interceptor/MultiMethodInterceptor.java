/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.framework.core.aop.interceptor;

import org.aopalliance.intercept.MethodInterceptor;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/11/13 13:54<i/>
 * @version 1.0
 */


public interface MultiMethodInterceptor extends MethodInterceptor {
    void addAdvice(AdvicePoint point);

    AdviceChain getMethodChain();
}
