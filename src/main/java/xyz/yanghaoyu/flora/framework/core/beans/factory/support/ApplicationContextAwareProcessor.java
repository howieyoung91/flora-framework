/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.framework.core.beans.factory.support;

import xyz.yanghaoyu.flora.framework.core.Ordered;
import xyz.yanghaoyu.flora.framework.core.PriorityOrdered;
import xyz.yanghaoyu.flora.framework.core.beans.factory.ApplicationContextAware;
import xyz.yanghaoyu.flora.framework.core.beans.factory.ApplicationEventPublisherAware;
import xyz.yanghaoyu.flora.framework.core.beans.factory.ResourceLoaderAware;
import xyz.yanghaoyu.flora.framework.core.beans.factory.config.BeanPostProcessor;
import xyz.yanghaoyu.flora.framework.core.context.ApplicationContext;
import xyz.yanghaoyu.flora.framework.exception.BeansException;

public class ApplicationContextAwareProcessor implements BeanPostProcessor, PriorityOrdered {
    private ApplicationContext applicationContext;

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE / 2;
    }

    public ApplicationContextAwareProcessor(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof ApplicationContextAware) {
            ((ApplicationContextAware) bean).setApplicationContext(applicationContext);
        }
        if (bean instanceof ResourceLoaderAware) {
            ((ResourceLoaderAware) bean).setResourceLoader(applicationContext);
        }
        if (bean instanceof ApplicationEventPublisherAware) {
            ((ApplicationEventPublisherAware) bean).setApplicationEventPublisher(applicationContext);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
