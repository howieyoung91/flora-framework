package xyz.yanghaoyu.flora.factory.support;

import xyz.yanghaoyu.flora.factory.config.BeanDefinition;

/**
 * @author <a href="https://www.yanghaoyu.xyz">Howie Young</a><i>on 2021/8/7 20:46<i/>
 * @version 1.0
 */


public interface BeanDefinitionRegistry {
    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);
}
