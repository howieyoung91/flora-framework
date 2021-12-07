package xyz.yanghaoyu.flora.core.beans.factory.config;

import cn.hutool.core.util.TypeUtil;
import xyz.yanghaoyu.flora.annotation.Inject;
import xyz.yanghaoyu.flora.annotation.Value;
import xyz.yanghaoyu.flora.core.beans.factory.BeanFactory;
import xyz.yanghaoyu.flora.core.beans.factory.BeanFactoryAware;
import xyz.yanghaoyu.flora.core.beans.factory.ConfigurableListableBeanFactory;
import xyz.yanghaoyu.flora.core.beans.factory.PropertyValues;
import xyz.yanghaoyu.flora.core.convert.converter.ConversionService;
import xyz.yanghaoyu.flora.exception.BeansException;
import xyz.yanghaoyu.flora.exception.DuplicateDeclarationException;
import xyz.yanghaoyu.flora.util.ReflectUtil;
import xyz.yanghaoyu.flora.util.StringUtil;

import java.lang.reflect.Field;
import java.util.Map;

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
            // 1. 处理注解 @Value
            handleValueAnnotation(bean, field);
            // 2. 处理注解 @Inject.ByType 和 @Inject.ByName
            handleInjectAnnotation(bean, beanName, field);
        }

        return null;
    }

    @Inject.ByType(clazz = String.class, required = false)
    private String temp;

    private void handleInjectAnnotation(Object bean, String beanName, Field field) {
        Inject.ByType injectByTypeAnno = field.getAnnotation(Inject.ByType.class);
        Inject.ByName injectByNameAnno = field.getAnnotation(Inject.ByName.class);
        if (injectByNameAnno != null && injectByTypeAnno != null) {
            throw new DuplicateDeclarationException("the [@Inject.ByType] and [@Inject.ByName] are duplicate!");
        }
        // 处理 Inject.ByType
        if (injectByTypeAnno != null) {
            // determine the clazz
            Class value = injectByTypeAnno.value();
            Class clz = injectByTypeAnno.clazz();

            Class clazz = value == clz
                    ? value
                    : clz == Inject.ByType.DEFAULT_CLASS ? value : clz;

            if (clazz == Inject.ByType.DEFAULT_CLASS) {
                clazz = field.getType();
            }

            Map<String, ?> candidates = beanFactory.getBeansOfType(clazz);
            if (candidates.size() == 1) {
                for (Object dependOnBean : candidates.values()) {
                    ReflectUtil.setFieldValue(bean, field.getName(), dependOnBean);
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
            ReflectUtil.setFieldValue(bean, field.getName(), dependOnBean);
        }
    }

    private void handleValueAnnotation(Object bean, Field field) {
        Value valueAnn = field.getAnnotation(Value.class);
        if (null != valueAnn) {
            Object value = valueAnn.value();
            value = beanFactory.resolveEmbeddedValue((String) value);
            if (value == null) {
                if (valueAnn.required()) {
                    throw new BeansException("Fail to find the value [" + valueAnn.value() + "]");
                }
            }
            // 类型转换


            Class<?> sourceType = value.getClass();
            Class<?> targetType = (Class<?>) TypeUtil.getType(field);
            ConversionService conversionService = beanFactory.getConversionService();
            if (conversionService != null) {
                if (conversionService.canConvert(sourceType, targetType)) {
                    value = conversionService.convert(value, targetType);
                }
            }
            ReflectUtil.setFieldValue(bean, field.getName(), value);
        }
    }
}

