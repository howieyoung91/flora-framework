package xyz.yanghaoyu.flora.beans.factory.support;

import xyz.yanghaoyu.flora.beans.factory.config.BeanDefinition;

import java.lang.reflect.Constructor;


public interface InstantiationStrategy {
    Object instantiate(BeanDefinition beanDefinition, Constructor ctor, Object[] args);
}
