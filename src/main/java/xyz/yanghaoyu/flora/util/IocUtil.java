package xyz.yanghaoyu.flora.util;

import xyz.yanghaoyu.flora.constant.BuiltInBean;
import xyz.yanghaoyu.flora.core.beans.factory.PropertyValue;
import xyz.yanghaoyu.flora.core.beans.factory.PropertyValues;
import xyz.yanghaoyu.flora.core.beans.factory.config.BeanDefinition;
import xyz.yanghaoyu.flora.core.beans.factory.support.BeanDefinitionRegistry;
import xyz.yanghaoyu.flora.core.beans.factory.support.DefaultListableBeanFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        if (!registry.containsBeanDefinition(BuiltInBean.ANNOTATION_AWARE_ASPECT_J_AUTO_PROXY_SUPPORT_BEAN_FACTORY_POST_PROCESSOR.getName())) {
            registry.registerBeanDefinition(
                    BuiltInBean.ANNOTATION_AWARE_ASPECT_J_AUTO_PROXY_SUPPORT_BEAN_FACTORY_POST_PROCESSOR.getName(),
                    new BeanDefinition(BuiltInBean.ANNOTATION_AWARE_ASPECT_J_AUTO_PROXY_SUPPORT_BEAN_FACTORY_POST_PROCESSOR));
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
        // @Inject, @Value Support
        if (!registry.containsBeanDefinition(BuiltInBean.AUTOWIRED_ANNOTATION_BEAN_POST_PROCESSOR.getName())) {
            registry.registerBeanDefinition(
                    BuiltInBean.AUTOWIRED_ANNOTATION_BEAN_POST_PROCESSOR.getName(),
                    new BeanDefinition(BuiltInBean.AUTOWIRED_ANNOTATION_BEAN_POST_PROCESSOR)
            );
        }
        // @Configuration Support
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
    public static void enablePropertyPlaceholderConfigurer(BeanDefinitionRegistry registry, String... locations) {
        BeanDefinition beanDef = null;
        Class beanClass = BuiltInBean.PROPERTY_PLACEHOLDER_CONFIGURER;
        String beanName = beanClass.getName();

        if (!registry.containsBeanDefinition(beanName)) {
            beanDef = new BeanDefinition(beanClass);
            beanDef.getPropertyValues()
                    .addPropertyValue(new PropertyValue("locations", new ArrayList<>(Arrays.asList(locations))));
            registry.registerBeanDefinition(beanName, beanDef);
        } else {
            beanDef = ((DefaultListableBeanFactory) registry).getBeanDefinition(beanName);
            PropertyValues propertyValues = beanDef.getPropertyValues();
            PropertyValue pv = propertyValues.getPropertyValue("locations");
            List value = (List) pv.getValue();
            value.addAll(Arrays.asList(locations));
        }
    }
}
