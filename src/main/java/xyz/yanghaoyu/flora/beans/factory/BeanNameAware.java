package xyz.yanghaoyu.flora.beans.factory;

import xyz.yanghaoyu.flora.BeansException;


public interface BeanClassLoaderAware extends Aware {
    void seBeanClassLoader(ClassLoader classLoader) throws BeansException;
}
