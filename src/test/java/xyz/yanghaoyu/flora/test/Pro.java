package xyz.yanghaoyu.flora.test;

import xyz.yanghaoyu.flora.BeansException;
import xyz.yanghaoyu.flora.beans.factory.ConfigurableListableBeanFactory;
import xyz.yanghaoyu.flora.beans.factory.config.BeanFactoryPostProcessor;

public class Pro implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        System.out.println("BeanFactoryPostProcessor!");
    }
}
