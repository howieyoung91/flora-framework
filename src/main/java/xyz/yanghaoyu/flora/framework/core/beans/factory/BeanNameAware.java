/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.framework.core.beans.factory;

import xyz.yanghaoyu.flora.framework.exception.BeansException;


public interface BeanNameAware extends Aware {
    void setBeanName(String beanName) throws BeansException;
}
