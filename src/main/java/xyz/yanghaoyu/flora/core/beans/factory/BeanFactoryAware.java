package xyz.yanghaoyu.flora.core.beans.factory;

import xyz.yanghaoyu.flora.exception.BeansException;


public interface BeanFactoryAware extends Aware {
    void setBeanFactory(BeanFactory beanFactory) throws BeansException;
}
