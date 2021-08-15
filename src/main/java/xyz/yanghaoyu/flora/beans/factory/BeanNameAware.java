package xyz.yanghaoyu.flora.beans.factory;

import xyz.yanghaoyu.flora.exception.BeansException;


public interface BeanNameAware extends Aware {
    void setBeanName(String beanName) throws BeansException;
}
