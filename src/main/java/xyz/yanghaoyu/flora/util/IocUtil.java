package xyz.yanghaoyu.flora.util;

import xyz.yanghaoyu.flora.constant.BuiltInBean;
import xyz.yanghaoyu.flora.core.beans.factory.ConfigurableListableBeanFactory;
import xyz.yanghaoyu.flora.core.beans.factory.PropertyPlaceholderConfigurer;
import xyz.yanghaoyu.flora.core.beans.factory.PropertyValue;
import xyz.yanghaoyu.flora.core.beans.factory.PropertyValues;
import xyz.yanghaoyu.flora.core.beans.factory.config.BeanDefinition;
import xyz.yanghaoyu.flora.core.beans.factory.support.BeanDefinitionRegistry;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public abstract class IocUtil {
    /**
     * 开启 aop
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
     * <p>
     * Support Annotations:
     * <ol>
     *     <li> @Inject </li>
     *     <li> @Value </li>
     *     <li> @Configuration </li>
     * </ol>
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

        enableInitDestroyAnnotation(registry);
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
        String resourcesLocationsFieldName = PropertyPlaceholderConfigurer.LOCATIONS;

        if (!registry.containsBeanDefinition(beanName)) {
            beanDef = new BeanDefinition(beanClass);
            beanDef.getPropertyValues()
                    .addPropertyValue(new PropertyValue(resourcesLocationsFieldName, new HashSet<>(Arrays.asList(locations))));
            registry.registerBeanDefinition(beanName, beanDef);
        } else {
            beanDef = ((ConfigurableListableBeanFactory) registry).getBeanDefinition(beanName);
            PropertyValues propertyValues = beanDef.getPropertyValues();
            PropertyValue pv = propertyValues.getPropertyValue(resourcesLocationsFieldName);
            Set value = (Set) pv.getValue();
            value.addAll(Arrays.asList(locations));
        }
    }

    /**
     * Support Annotations:
     * <ol>
     *     <li> @PostConstruct </li>
     *     <li> @Life.Initialize </li>
     *     <li> @PreDestroy </li>
     *     <li> @Life.Destroy </li>
     * </ol>
     */
    public static void enableInitDestroyAnnotation(BeanDefinitionRegistry registry) {
        if (!registry.containsBeanDefinition(BuiltInBean.INIT_DESTROY_ANNOTATION_BEAN_POST_PROCESSOR.getName())) {
            registry.registerBeanDefinition(
                    BuiltInBean.INIT_DESTROY_ANNOTATION_BEAN_POST_PROCESSOR.getName(),
                    new BeanDefinition(BuiltInBean.INIT_DESTROY_ANNOTATION_BEAN_POST_PROCESSOR)
            );
        }
    }
}
