package xyz.yanghaoyu.flora.core.beans.factory;


import xyz.yanghaoyu.flora.exception.BeansException;

public interface BeanFactory {
    Object getBean(String name) throws BeansException;

    Object getBean(String name, Object... args) throws BeansException;

    <T> T getBean(String name, Class<T> requiredType);

    <T> T getBean(Class<T> type);

    boolean containsBean(String name);

    boolean containsSingletonBean(String name);
}
