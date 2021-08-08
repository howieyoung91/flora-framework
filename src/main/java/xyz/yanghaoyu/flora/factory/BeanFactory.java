package xyz.yanghaoyu.flora.factory;


import xyz.yanghaoyu.flora.BeansException;

public interface BeanFactory {
    Object getBean(String name) throws BeansException;

    Object getBean(String name, Object... args) throws BeansException;
}
