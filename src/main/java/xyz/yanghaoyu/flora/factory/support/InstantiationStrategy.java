package xyz.yanghaoyu.flora.factory.support;

import xyz.yanghaoyu.flora.factory.config.BeanDefinition;

import java.lang.reflect.Constructor;


public interface InstantiationStrategy {
    Object instantiate(BeanDefinition beanDefinition, Constructor ctor, Object[] args);
}
