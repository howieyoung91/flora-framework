package xyz.yanghaoyu.flora.test.bean;

import xyz.yanghaoyu.flora.exception.BeansException;
import xyz.yanghaoyu.flora.annotation.Component;
import xyz.yanghaoyu.flora.beans.factory.ConfigurableListableBeanFactory;
import xyz.yanghaoyu.flora.beans.factory.config.BeanFactoryPostProcessor;

@Component
public class Pro implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        System.out.println("BeanFactoryPostProcessor!");
    }
}
