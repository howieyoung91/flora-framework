/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.framework.core.beans.factory.config;

import xyz.yanghaoyu.flora.framework.core.beans.factory.ConfigurableListableBeanFactory;
import xyz.yanghaoyu.flora.framework.core.beans.factory.support.BeanDefinitionRegistry;
import xyz.yanghaoyu.flora.framework.exception.BeansException;

public interface BeanDefinitionRegistryPostProcessor extends BeanFactoryPostProcessor {
    void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException;

    @Override
    default void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
    }
}
