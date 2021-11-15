package xyz.yanghaoyu.flora.core.beans.factory.support;

import xyz.yanghaoyu.flora.exception.BeansException;
import xyz.yanghaoyu.flora.core.beans.factory.ApplicationContextAware;
import xyz.yanghaoyu.flora.core.beans.factory.config.BeanPostProcessor;
import xyz.yanghaoyu.flora.core.context.ApplicationContext;

public class ApplicationContextAwareProcessor implements BeanPostProcessor {
    ApplicationContext applicationContext;

    public ApplicationContextAwareProcessor(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof ApplicationContextAware) {
            ((ApplicationContextAware) bean).setApplicationContext(applicationContext);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
