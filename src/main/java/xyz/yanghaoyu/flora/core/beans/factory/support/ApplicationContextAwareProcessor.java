package xyz.yanghaoyu.flora.core.beans.factory.support;

import xyz.yanghaoyu.flora.core.Ordered;
import xyz.yanghaoyu.flora.core.PriorityOrdered;
import xyz.yanghaoyu.flora.core.beans.factory.ApplicationContextAware;
import xyz.yanghaoyu.flora.core.beans.factory.ResourceLoaderAware;
import xyz.yanghaoyu.flora.core.beans.factory.config.BeanPostProcessor;
import xyz.yanghaoyu.flora.core.context.ApplicationContext;
import xyz.yanghaoyu.flora.exception.BeansException;

public class ApplicationContextAwareProcessor implements BeanPostProcessor, PriorityOrdered {
    private ApplicationContext applicationContext;

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE / 2;
    }

    public ApplicationContextAwareProcessor(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof ApplicationContextAware) {
            ((ApplicationContextAware) bean).setApplicationContext(applicationContext);
        }
        if (bean instanceof ResourceLoaderAware) {
            ((ResourceLoaderAware) bean).setResourceLoader(applicationContext);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
