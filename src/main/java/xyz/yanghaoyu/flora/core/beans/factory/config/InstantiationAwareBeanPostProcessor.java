package xyz.yanghaoyu.flora.core.beans.factory.config;

import xyz.yanghaoyu.flora.core.beans.factory.PropertyValues;
import xyz.yanghaoyu.flora.exception.BeansException;

public interface InstantiationAwareBeanPostProcessor extends BeanPostProcessor {
    default Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        return null;
    }

    /**
     * 在 Bean 对象执行实例化之后，执行此方法 返回false, BeanFactory 将不再填充属性
     */
    default boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        return true;
    }

    /**
     * 在 Bean 对象执行实例化之后 初始化之前 进行属性的修改
     */
    default PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName) throws BeansException {
        return pvs;
    }
}