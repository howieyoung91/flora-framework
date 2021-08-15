package xyz.yanghaoyu.flora.beans.factory;

import xyz.yanghaoyu.flora.exception.BeansException;


public interface BeanClassLoaderAware extends Aware {
    void setBeanClassLoader(ClassLoader classLoader) throws BeansException;
}
