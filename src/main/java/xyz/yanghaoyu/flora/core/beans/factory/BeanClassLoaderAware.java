package xyz.yanghaoyu.flora.core.beans.factory;

import xyz.yanghaoyu.flora.exception.BeansException;


public interface BeanClassLoaderAware extends Aware {
    void setBeanClassLoader(ClassLoader classLoader) throws BeansException;
}
