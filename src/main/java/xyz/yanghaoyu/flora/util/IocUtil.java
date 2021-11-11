package xyz.yanghaoyu.flora.util;

import xyz.yanghaoyu.flora.aop.autoproxy.DefaultAdvisorAutoProxyCreator;
import xyz.yanghaoyu.flora.beans.factory.PropertyPlaceholderConfigurer;
import xyz.yanghaoyu.flora.beans.factory.PropertyValue;
import xyz.yanghaoyu.flora.beans.factory.config.AutowiredAnnotationBeanPostProcessor;
import xyz.yanghaoyu.flora.beans.factory.config.BeanDefinition;
import xyz.yanghaoyu.flora.beans.factory.support.BeanDefinitionRegistry;

public class IocUtil {
    private IocUtil() { }

    /**
     * 开启aop
     */
    public static void enableAop(BeanDefinitionRegistry registry) {
        registry.registerBeanDefinition(DefaultAdvisorAutoProxyCreator.class.getName(), new BeanDefinition(DefaultAdvisorAutoProxyCreator.class));
    }

    /**
     * 开启组件扫描
     */
    public static void enableComponentScan(BeanDefinitionRegistry registry) {
        registry.registerBeanDefinition(AutowiredAnnotationBeanPostProcessor.class.getName(), new BeanDefinition(AutowiredAnnotationBeanPostProcessor.class));
    }

    /**
     * 开启占位符替换
     */
    public static void enablePropertyPlaceholderConfigurer(BeanDefinitionRegistry registry, String location) {
        BeanDefinition propertyPlaceholderConfigurerBeanDefinition =
                new BeanDefinition(PropertyPlaceholderConfigurer.class);
        propertyPlaceholderConfigurerBeanDefinition
                .getPropertyValues()
                .addPropertyValue(new PropertyValue("location", location));
        registry.registerBeanDefinition(PropertyPlaceholderConfigurer.class.getName(), propertyPlaceholderConfigurerBeanDefinition);
    }
}
