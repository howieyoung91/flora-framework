package xyz.yanghaoyu.flora.beans.factory.config;

import xyz.yanghaoyu.flora.exception.BeansException;
import xyz.yanghaoyu.flora.beans.factory.ConfigurableListableBeanFactory;


public interface BeanFactoryPostProcessor {
    void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException;
}
