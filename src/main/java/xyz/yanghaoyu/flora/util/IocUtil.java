package xyz.yanghaoyu.flora.util;

import xyz.yanghaoyu.flora.constant.BuiltInBean;
import xyz.yanghaoyu.flora.core.beans.factory.PropertyValue;
import xyz.yanghaoyu.flora.core.beans.factory.config.AnnotationAwareAspectJAutoProxySupportBeanFactoryPostProcessor;
import xyz.yanghaoyu.flora.core.beans.factory.config.BeanDefinition;
import xyz.yanghaoyu.flora.core.beans.factory.support.BeanDefinitionRegistry;

public abstract class IocUtil {
    /**
     * 开启aop
     */
    public static void enableAop(BeanDefinitionRegistry registry) {
        // 不支持 XML AOP
        // registry.registerBeanDefinition(
        //         DefaultAdvisorAutoProxyCreator.class.getName(),
        //         new BeanDefinition(DefaultAdvisorAutoProxyCreator.class)
        // );
        // 注解 AOP
        if (!registry.containsBeanDefinition(AnnotationAwareAspectJAutoProxySupportBeanFactoryPostProcessor.class.getName())) {
            registry.registerBeanDefinition(AnnotationAwareAspectJAutoProxySupportBeanFactoryPostProcessor.class.getName(), new BeanDefinition(AnnotationAwareAspectJAutoProxySupportBeanFactoryPostProcessor.class));
            registry.registerBeanDefinition(
                    BuiltInBean.ANNOTATION_AWARE_ASPECT_J_AUTO_PROXY_CREATOR.getName(),
                    new BeanDefinition(BuiltInBean.ANNOTATION_AWARE_ASPECT_J_AUTO_PROXY_CREATOR)
            );
        }
    }

    /**
     * 开启组件扫描
     */
    public static void enableComponentScan(BeanDefinitionRegistry registry) {
        if (!registry.containsBeanDefinition(BuiltInBean.AUTOWIRED_ANNOTATION_BEAN_POST_PROCESSOR.getName())) {
            registry.registerBeanDefinition(
                    BuiltInBean.AUTOWIRED_ANNOTATION_BEAN_POST_PROCESSOR.getName(),
                    new BeanDefinition(BuiltInBean.AUTOWIRED_ANNOTATION_BEAN_POST_PROCESSOR)
            );
        }
        if (!registry.containsBeanDefinition(BuiltInBean.CONFIGURATION_BEAN_BEAN_FACTORY_POST_PROCESSOR.getName())) {
            registry.registerBeanDefinition(
                    BuiltInBean.CONFIGURATION_BEAN_BEAN_FACTORY_POST_PROCESSOR.getName(),
                    new BeanDefinition(BuiltInBean.CONFIGURATION_BEAN_BEAN_FACTORY_POST_PROCESSOR)
            );
        }
    }

    public static void enableTypeConvert(BeanDefinitionRegistry registry) {
        if (!registry.containsBeanDefinition(BuiltInBean.CONVERTER_FACTORY_BEAN.getName())) {
            registry.registerBeanDefinition(
                    BuiltInBean.CONVERTER_FACTORY_BEAN.getName(),
                    new BeanDefinition(BuiltInBean.CONVERTER_FACTORY_BEAN)
            );
        }
    }

    /**
     * 开启占位符替换
     */
    public static void enablePropertyPlaceholderConfigurer(BeanDefinitionRegistry registry, String location) {
        if (!registry.containsBeanDefinition(BuiltInBean.PROPERTY_PLACEHOLDER_CONFIGURER.getName())) {
            BeanDefinition propertyPlaceholderConfigurerBeanDefinition =
                    new BeanDefinition(BuiltInBean.PROPERTY_PLACEHOLDER_CONFIGURER);
            propertyPlaceholderConfigurerBeanDefinition
                    .getPropertyValues()
                    .addPropertyValue(new PropertyValue("location", location));
            registry.registerBeanDefinition(
                    BuiltInBean.PROPERTY_PLACEHOLDER_CONFIGURER.getName(),
                    propertyPlaceholderConfigurerBeanDefinition
            );
        }
    }
}
