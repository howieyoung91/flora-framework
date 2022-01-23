package xyz.yanghaoyu.flora.core.beans.factory.config;

import xyz.yanghaoyu.flora.annotation.ConfigurationProperties;
import xyz.yanghaoyu.flora.annotation.Inject;
import xyz.yanghaoyu.flora.annotation.Value;
import xyz.yanghaoyu.flora.core.beans.factory.BeanFactory;
import xyz.yanghaoyu.flora.core.beans.factory.BeanFactoryAware;
import xyz.yanghaoyu.flora.core.beans.factory.ConfigurableListableBeanFactory;
import xyz.yanghaoyu.flora.core.beans.factory.PropertyValues;
import xyz.yanghaoyu.flora.exception.BeansException;
import xyz.yanghaoyu.flora.exception.DuplicateDeclarationException;
import xyz.yanghaoyu.flora.util.ConversionUtil;
import xyz.yanghaoyu.flora.util.PropertyUtil;
import xyz.yanghaoyu.flora.util.ReflectUtil;
import xyz.yanghaoyu.flora.util.StringUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AutowiredAnnotationBeanPostProcessor implements InstantiationAwareBeanPostProcessor, BeanFactoryAware {
    private ConfigurableListableBeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
    }

    @Override
    public PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName) throws BeansException {
        Class<?> clazz = bean.getClass();

        clazz = ReflectUtil.isCglibProxyClass(clazz) ? clazz.getSuperclass() : clazz;

        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            handleValueAnnotation(bean, field, clazz);
            handleInjectAnnotation(bean, beanName, field, clazz);
        }
        handleConfigurationPropertiesAnnotation(bean, clazz);
        return null;
    }

    private void handleInjectAnnotation(Object bean, String beanName, Field field, Class actualClass) {
        Inject.ByType injectByTypeAnno = field.getAnnotation(Inject.ByType.class);
        Inject.ByName injectByNameAnno = field.getAnnotation(Inject.ByName.class);
        if (injectByNameAnno != null && injectByTypeAnno != null) {
            throw new DuplicateDeclarationException("the [@Inject.ByType] and [@Inject.ByName] are duplicate!");
        }
        // handle @Inject.ByType
        if (injectByTypeAnno != null) {
            // determine the dependOnBeanClass
            Class value = injectByTypeAnno.value();
            Class clz = injectByTypeAnno.clazz();

            Class dependOnBeanClass = value == clz
                    ? value
                    : clz == Inject.ByType.DEFAULT_CLASS ? value : clz;

            if (dependOnBeanClass == Inject.ByType.DEFAULT_CLASS) {
                dependOnBeanClass = field.getType();
            }

            // select a candidate
            Map<String, ?> candidates = beanFactory.getBeansOfType(dependOnBeanClass);
            if (candidates.size() == 1) {
                for (Object dependOnBean : candidates.values()) {
                    ReflectUtil.setFieldValue(bean, actualClass, field.getName(), dependOnBean);
                }
            } else {
                if (injectByTypeAnno.required()) {
                    if (candidates.size() > 1) {
                        throw new BeansException("multiple beans are candidate! at: " + beanName + "field: " + field.getName() + ". Please use Inject.ByName");
                    } else {
                        throw new BeansException("No such Bean which class is  " + field.getType().getName() + " when gen " + beanName);
                    }
                }
            }
        }

        // 处理 Inject.ByName
        if (injectByNameAnno != null) {
            String value = injectByNameAnno.value();
            String id = injectByNameAnno.id();
            if (StringUtil.isEmpty(id)) {
                id = StringUtil.isEmpty(value)
                        ? StringUtil.lowerFirstChar(field.getName())
                        : value;
            }
            Object dependOnBean = beanFactory.getBean(id);
            if (dependOnBean == null && injectByNameAnno.required()) {
                throw new BeansException("No such Bean which id is  " + id);
            }
            ReflectUtil.setFieldValue(bean, actualClass, field.getName(), dependOnBean);
        }
    }

    private void handleValueAnnotation(Object bean, Field field, Class<?> clazz) {
        Value valueAnn = field.getAnnotation(Value.class);
        if (null != valueAnn) {
            Object value = valueAnn.value();
            value = beanFactory.resolveEmbeddedValue((String) value);

            if (value == null) {
                if (valueAnn.required()) {
                    throw new BeansException("Fail to find the value [" + valueAnn.value() + "]");
                }
            } else {
                // 类型转换
                value = ConversionUtil.convertField(field, value, beanFactory);
            }
            ReflectUtil.setFieldValue(bean, clazz, field.getName(), value);
        }
    }

    private static final Set SET = new HashSet(3);

    static {
        SET.add(Value.class);
        SET.add(Inject.ByName.class);
        SET.add(Inject.ByType.class);
    }

    private void handleConfigurationPropertiesAnnotation(Object bean, Class<?> actualClass) {
        ConfigurationProperties configPropertiesAnn = actualClass.getAnnotation(ConfigurationProperties.class);
        if (configPropertiesAnn == null) {
            return;
        }
        String prefix = configPropertiesAnn.prefix();
        Field[] declaredFields = actualClass.getDeclaredFields();
        for (Field field : declaredFields) {
            if (!shouldConfig(field)) {
                continue;
            }
            String key = PropertyUtil.createPropertyKey(prefix + "." + field.getName());
            Object value = beanFactory.resolveEmbeddedValue(key);
            if (value == null) {
                continue;
            }
            value = ConversionUtil.convertField(field, value, beanFactory);
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

