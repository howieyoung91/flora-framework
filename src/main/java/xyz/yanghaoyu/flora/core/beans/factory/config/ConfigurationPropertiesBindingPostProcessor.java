package xyz.yanghaoyu.flora.core.beans.factory.config;

import xyz.yanghaoyu.flora.annotation.ConfigurationProperties;
import xyz.yanghaoyu.flora.annotation.Inject;
import xyz.yanghaoyu.flora.annotation.Value;
import xyz.yanghaoyu.flora.core.Ordered;
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
        implements BeanPostProcessor, BeanFactoryAware, Ordered {
    private ConfigurableListableBeanFactory beanFactory;

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE / 2 + 2;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        BeanDefinition beanDef = beanFactory.getBeanDefinition(beanName);
        Class<?>       clazz   = ReflectUtil.getBeanClassFromCglibProxy(bean.getClass());

        Method factoryMethod = beanDef.getFactoryMethod();
        if (factoryMethod == null) {
            handle(bean, clazz);
        } else {
            handle(bean, factoryMethod);
        }
        return bean;
    }

    private static final Set SET = new HashSet(3);

    static {
        SET.add(Value.class);
        SET.add(Inject.ByName.class);
        SET.add(Inject.ByType.class);
    }

    private void handle(Object bean, Method factoryMethod) {
        ConfigurationProperties configPropertiesAnn = factoryMethod.getAnnotation(ConfigurationProperties.class);
        Class<?>                actualClass         = factoryMethod.getReturnType();
        // 如果 factoryMethod 上没有注解，则看看类上是否存在注解
        if (configPropertiesAnn == null) {
            configPropertiesAnn = actualClass.getAnnotation(ConfigurationProperties.class);
        }
        inject(bean, actualClass, configPropertiesAnn);
    }

    private void handle(Object bean, Class<?> actualClass) {
        ConfigurationProperties configPropertiesAnn = actualClass.getAnnotation(ConfigurationProperties.class);
        inject(bean, actualClass, configPropertiesAnn);
    }

    private void inject(Object bean, Class<?> actualClass, ConfigurationProperties configPropertiesAnn) {
        if (configPropertiesAnn == null) {
            return;
        }

        String  prefix = configPropertiesAnn.prefix();
        Field[] fields = actualClass.getDeclaredFields();
        for (Field field : fields) {
            if (shouldConfigure(field)) {
                String key   = PropertyUtil.createPropertyKey(prefix + "." + field.getName());
                Object value = beanFactory.resolveEmbeddedValue(key);
                if (value == null) {
                    continue;
                }
                value = ConversionUtil.convertField(field, value, beanFactory.getConversionService());
                ReflectUtil.setFieldValue(bean, actualClass, field.getName(), value);
            }
        }
    }

    private boolean shouldConfigure(Field field) {
        Annotation[] annotations = field.getAnnotations();
        for (Annotation annotation : annotations) {
            if (SET.contains(annotation.annotationType())) {
                return false;
            }
        }
        return true;
    }
}
