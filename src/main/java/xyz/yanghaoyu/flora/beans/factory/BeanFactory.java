package xyz.yanghaoyu.flora.beans.factory;


import xyz.yanghaoyu.flora.BeansException;

public interface BeanFactory {
    Object getBean(String name) throws BeansException;

    Object getBean(String name, Object... args) throws BeansException;

    <T> T getBean(String name, Class<T> requiredType);
}
