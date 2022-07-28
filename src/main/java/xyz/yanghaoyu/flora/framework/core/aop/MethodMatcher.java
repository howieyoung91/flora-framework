/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.framework.core.aop;

import java.lang.reflect.Method;

/**
 * 方法匹配器
 * <p>
 * 在目标类下找到对应的方法
 */

public interface MethodMatcher {
    boolean matches(Method method, Class<?> targetClass);
}
