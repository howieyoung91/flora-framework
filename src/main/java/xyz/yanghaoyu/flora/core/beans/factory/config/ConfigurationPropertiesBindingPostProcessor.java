package xyz.yanghaoyu.flora.core.beans.factory.config;

import xyz.yanghaoyu.flora.annotation.ConfigurationProperties;
import xyz.yanghaoyu.flora.annotation.Inject;
import xyz.yanghaoyu.flora.annotation.Value;
import xyz.yanghaoyu.flora.core.Ordered;
import xyz.yanghaoyu.flora.core.PriorityOrdered;
import xyz.yanghaoyu.flora.core.beans.factory.BeanFactory;
import xyz.yanghaoyu.flora.core.beans.factory.BeanFactoryAware;
import xyz.yanghaoyu.flora.core.beans.factory.ConfigurableListableBeanFactory;
import xyz.yanghaoyu.flora.exception.BeansException;
import xyz.yanghaoyu.flora.util.ConversionUtil;
import xyz.yanghaoyu.flora.util.PropertyUtil;
import xyz.yanghaoyu.flora.util.ReflectUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * Support @ConfigurationProperties
 *
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2022/2/8 17:54<i/>
 * @version 1.0
 */

public class ConfigurationPropertiesBindingPostProcessor
        implements BeanPostProcessor, PriorityOrdered, BeanFactoryAware {
    private ConfigurableListableBeanFactory beanFactory;

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 1;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        BeanDefinition beanDef = beanFactory.getBeanDefinition(beanName);
        Class<?>       clazz   = bean.getClass();
        clazz = ReflectUtil.isCglibProxyClass(clazz) ? clazz.getSuperclass() : clazz;
        Method factoryMethod = beanDef.getFactoryMethod();
        if (factoryMethod == null) {
            handleConfigurationPropertiesAnnotation(bean, clazz);
        } else {
            handleConfigurationPropertiesAnnotation(bean, factoryMethod);
        }

        return bean;
    }

    private static final Set SET = new HashSet(3);

    static {
        SET.add(Value.class);
        SET.add(Inject.ByName.class);
        SET.add(Inject.ByType.class);
    }

    private void handleConfigurationPropertiesAnnotation(Object bean, Method factoryMethod) {
        ConfigurationProperties configurationProperties = factoryMethod.getAnnotation(ConfigurationProperties.class);
        Class<?>                actualClass             = factoryMethod.getReturnType();
        inject(bean, actualClass, configurationProperties);
    }

    private void handleConfigurationPropertiesAnnotation(Object bean, Class<?> actualClass) {
        ConfigurationProperties configPropertiesAnn = actualClass.getAnnotation(ConfigurationProperties.class);
        inject(bean, actualClass, configPropertiesAnn);
    }

    private void inject(Object bean, Class<?> actualClass, ConfigurationProperties configPropertiesAnn) {
        if (configPropertiesAnn == null) {
            return;
        }
        String  prefix         = configPropertiesAnn.prefix();
        Field[] declaredFields = actualClass.getDeclaredFields();
        for (Field field : declaredFields) {
            if (!shouldConfig(field)) {
                continue;
            }
            String key   = PropertyUtil.createPropertyKey(prefix + "." + field.getName());
            Object value = beanFactory.resolveEmbeddedValue(key);
            if (value == null) {
                continue;
            }
            value = ConversionUtil.convertField(field, value, beanFactory.getConversionService());
            ReflectUtil.setFieldValue(bean, actualClass, field.getName(), value);
        }
    }

    private boolean shouldConfig(Field field) {
        Annotation[] annotations = field.getAnnotations();
        for (Annotation annotation : annotations) {
            if (SET.contains(annotation.annotationType())) {
                return false;
            }
        }
        return true;
    }
}
