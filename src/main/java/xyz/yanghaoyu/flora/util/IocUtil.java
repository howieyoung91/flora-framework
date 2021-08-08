package xyz.yanghaoyu.flora.util;

import xyz.yanghaoyu.flora.constant.BuiltInBean;
import xyz.yanghaoyu.flora.core.beans.factory.ConfigurableListableBeanFactory;
import xyz.yanghaoyu.flora.core.beans.factory.PropertyPlaceholderConfigurer;
import xyz.yanghaoyu.flora.core.beans.factory.PropertyValue;
import xyz.yanghaoyu.flora.core.beans.factory.PropertyValues;
import xyz.yanghaoyu.flora.core.beans.factory.config.BeanDefinition;
import xyz.yanghaoyu.flora.core.beans.factory.support.BeanDefinitionRegistry;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public abstract class IocUtil {
    /**
     * 开启 aop
     */
    public static void enableAop(BeanDefinitionRegistry registry) {
        // 不支持 XML AOP
        // registerBuiltinBeanIfNecessary(registry, BuiltInBean.DEFAULT_ADVISOR_AUTO_PROXY_CREATOR);

        // Annotation AOP
        registerBuiltinBeanIfNecessary(registry, BuiltInBean.AUTO_PROXY_SUPPORT_BEAN_FACTORY_POST_PROCESSOR);
        registerBuiltinBeanIfNecessary(registry, BuiltInBean.ANNOTATION_AWARE_ASPECT_J_AUTO_PROXY_CREATOR);
    }

    /**
     * 开启依赖注入注解
     * <p>
     * Support Annotations:
     * <ol>
     *     <li> @Inject </li>
     *     <li> @Value </li>
     *     <li> @ConfigurationProperties </li>
     * </ol>
     */
    public static void enableAutowiredAnnotations(BeanDefinitionRegistry registry) {
        // @Inject, @Value Support
        registerBuiltinBeanIfNecessary(registry, BuiltInBean.AUTOWIRED_ANNOTATION_BEAN_POST_PROCESSOR);
        // @ConfigurationProperties
        registerBuiltinBeanIfNecessary(registry, BuiltInBean.CONFIGURATION_PROPERTIES_BINDING_POST_PROCESSOR);

        enableJavaClassConfiguration(registry);
        enableInitDestroyAnnotations(registry);
    }

    /**
     * Support @Configuration
     */
    public static void enableJavaClassConfiguration(BeanDefinitionRegistry registry) {
        registerBuiltinBeanIfNecessary(registry, BuiltInBean.CONFIGURATION_BEAN_BEAN_FACTORY_POST_PROCESSOR);
    }

    public static void enableTypeConvert(BeanDefinitionRegistry registry) {
        registerBuiltinBeanIfNecessary(registry, BuiltInBean.CONVERTER_FACTORY_BEAN);
    }

    /**
     * 开启占位符替换
     */
    public static void enablePropertyPlaceholderConfigurer(BeanDefinitionRegistry registry, String... locations) {
        BeanDefinition beanDef                     = null;
        Class          beanClass                   = BuiltInBean.PROPERTY_PLACEHOLDER_CONFIGURER;
        String         beanName                    = BeanUtil.builtInBeanName(beanClass);
        String         resourcesLocationsFieldName = PropertyPlaceholderConfigurer.LOCATIONS;

        if (!registry.containsBeanDefinition(beanName)) {
            beanDef = new BeanDefinition(beanClass);
            beanDef.getPropertyValues()
                    .addPropertyValue(
                            new PropertyValue(
                                    resourcesLocationsFieldName,
                                    new LinkedHashSet<>(Arrays.asList(locations))
                            )
                    );
            registry.registerBeanDefinition(beanName, beanDef);
        }
        else {
            beanDef = ((ConfigurableListableBeanFactory) registry).getBeanDefinition(beanName);
            PropertyValues propertyValues = beanDef.getPropertyValues();
            PropertyValue  pv             = propertyValues.getPropertyValue(resourcesLocationsFieldName);
            Set            value          = (Set) pv.getValue();
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
    public static void enableInitDestroyAnnotations(BeanDefinitionRegistry registry) {
        registerBuiltinBeanIfNecessary(registry, BuiltInBean.INIT_DESTROY_ANNOTATION_BEAN_POST_PROCESSOR);
    }

    public static void enableTransaction(BeanDefinitionRegistry registry) {
        registerBuiltinBeanIfNecessary(registry, BuiltInBean.TRANSACTION_BEAN_FACTORY_POST_PROCESSOR);
    }

    private static void registerBuiltinBeanIfNecessary(BeanDefinitionRegistry registry, Class<?> clazz) {
        String beanName = BeanUtil.builtInBeanName(clazz);
        if (!registry.containsBeanDefinition(beanName)) {
            registry.registerBeanDefinition(beanName, new BeanDefinition(clazz));
        }
    }
}
