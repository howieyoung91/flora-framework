package xyz.yanghaoyu.flora.beans.factory.config;

public interface SmartInstantiationAwareBeanPostProcessor extends InstantiationAwareBeanPostProcessor {
    default Object getEarlyBeanReference(Object bean, String beanName) {
        return bean;
    }
}
