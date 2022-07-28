/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.framework.core.beans.factory.support;

import xyz.yanghaoyu.flora.framework.core.beans.factory.config.BeanDefinition;

import java.lang.reflect.Constructor;


public interface InstantiationStrategy {
    Object instantiate(BeanDefinition beanDefinition, Constructor ctor, Object[] args);
}
