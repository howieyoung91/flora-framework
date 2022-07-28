/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.framework.core.context.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.yanghaoyu.flora.framework.core.beans.factory.ConfigurableListableBeanFactory;
import xyz.yanghaoyu.flora.framework.core.beans.factory.support.DefaultListableBeanFactory;
import xyz.yanghaoyu.flora.framework.exception.BeansException;

/**
 * 这个类主要关注工厂的创建
 */
public abstract class AbstractRefreshableApplicationContext extends AbstractApplicationContext {
    public static final Logger                     LOGGER = LoggerFactory.getLogger(AbstractRefreshableApplicationContext.class);
    private             DefaultListableBeanFactory beanFactory;

    @Override
    protected void refreshBeanFactory() throws BeansException {
        LOGGER.trace("create [BeanFactory]");
        DefaultListableBeanFactory beanFactory = createBeanFactory();
        loadBeanDefinitions(beanFactory);
        this.beanFactory = beanFactory;
    }

    private DefaultListableBeanFactory createBeanFactory() {
        return new DefaultListableBeanFactory();
    }

    protected abstract void loadBeanDefinitions(DefaultListableBeanFactory beanFactory);

    @Override
    public final ConfigurableListableBeanFactory getBeanFactory() {
        return beanFactory;
    }

    public final DefaultListableBeanFactory getDefaultListableBeanFactory() {
        return this.beanFactory;
    }
}
