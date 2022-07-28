/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.framework.annotation;

import xyz.yanghaoyu.flora.framework.core.beans.factory.ConfigurableListableBeanFactory;
import xyz.yanghaoyu.flora.framework.core.beans.factory.support.BeanDefinitionRegistry;
import xyz.yanghaoyu.flora.framework.core.io.ResourceLoader;

public interface ConditionContext {
    BeanDefinitionRegistry getRegistry();

    ConfigurableListableBeanFactory getBeanFactory();

    ResourceLoader getResourceLoader();

    ClassLoader getClassLoader();
}
