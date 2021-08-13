package xyz.yanghaoyu.flora.beans.factory;

import xyz.yanghaoyu.flora.BeansException;
import xyz.yanghaoyu.flora.beans.factory.config.AutowireCapableBeanFactory;
import xyz.yanghaoyu.flora.beans.factory.config.BeanDefinition;
import xyz.yanghaoyu.flora.beans.factory.config.BeanPostProcessor;
import xyz.yanghaoyu.flora.beans.factory.config.ConfigurableBeanFactory;

/**
 * @author <a href="https://www.yanghaoyu.xyz">Howie Young</a><i>on 2021/8/9 11:31<i/>
 * @version 1.0
 */


public interface ConfigurableListableBeanFactory extends ListableBeanFactory, AutowireCapableBeanFactory, ConfigurableBeanFactory {

    BeanDefinition getBeanDefinition(String beanName) throws BeansException;

    void preInstantiateSingletons() throws BeansException;

    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);

}