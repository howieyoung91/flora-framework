/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.framework.core.aop.aspectj;

import xyz.yanghaoyu.flora.framework.core.aop.ClassFilter;
import xyz.yanghaoyu.flora.framework.core.aop.MethodMatcher;
import xyz.yanghaoyu.flora.framework.core.aop.Pointcut;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/11/12 20:01<i/>
 * @version 1.0
 */


public interface AbstractPointcut extends Pointcut, ClassFilter, MethodMatcher {

    default ClassFilter getClassFilter() {
        return this;
    }

    default MethodMatcher getMethodMatcher() {
        return this;
    }
}
