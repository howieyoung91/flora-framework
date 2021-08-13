package xyz.yanghaoyu.flora.beans.factory;

import xyz.yanghaoyu.flora.BeansException;


public interface BeanFactoryAware extends Aware {
    void setBeanFactory(BeanFactory beanFactory) throws BeansException;
}
