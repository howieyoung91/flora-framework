package xyz.yanghaoyu.flora.beans.factory.support;

import xyz.yanghaoyu.flora.BeansException;
import xyz.yanghaoyu.flora.beans.factory.ApplicationContextAware;
import xyz.yanghaoyu.flora.beans.factory.config.BeanPostProcessor;
import xyz.yanghaoyu.flora.context.ApplicationContext;

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
