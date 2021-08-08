package xyz.yanghaoyu.flora.core.beans.factory;

import xyz.yanghaoyu.flora.exception.BeansException;


public interface BeanNameAware extends Aware {
    void setBeanName(String beanName) throws BeansException;
}
