/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.framework.core.beans.factory.config;

import xyz.yanghaoyu.flora.framework.exception.BeansException;

/**
 * @author <a href="https://www.yanghaoyu.xyz">Howie Young</a><i>on 2021/8/9 11:28<i/>
 * @version 1.0
 */


public interface BeanPostProcessor {
    /**
     * 在 Bean 对象执行初始化方法之前，执行此方法
     */
    default Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    /**
     * 在 Bean 对象执行初始化方法之后，执行此方法
     */
    default Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
