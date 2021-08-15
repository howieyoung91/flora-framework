package xyz.yanghaoyu.flora.beans.factory;

import xyz.yanghaoyu.flora.exception.BeansException;


public interface BeanFactoryAware extends Aware {
    void setBeanFactory(BeanFactory beanFactory) throws BeansException;
}
