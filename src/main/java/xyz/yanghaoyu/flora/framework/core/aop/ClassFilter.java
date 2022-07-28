/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.framework.core.aop;

/**
 * 类匹配器
 */
public interface ClassFilter {
    boolean matches(Class<?> clazz);
}
