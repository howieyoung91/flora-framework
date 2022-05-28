/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.annotation;

import xyz.yanghaoyu.flora.core.beans.factory.config.BeanDefinition;

@FunctionalInterface
public interface Condition {
    boolean matches(ConditionContext context, BeanDefinition beanDef);
}
