package xyz.yanghaoyu.flora.beans.factory.config;

import xyz.yanghaoyu.flora.annotation.Inject;
import xyz.yanghaoyu.flora.annotation.Value;
import xyz.yanghaoyu.flora.beans.factory.BeanFactory;
import xyz.yanghaoyu.flora.beans.factory.BeanFactoryAware;
import xyz.yanghaoyu.flora.beans.factory.ConfigurableListableBeanFactory;
import xyz.yanghaoyu.flora.beans.factory.PropertyValues;
import xyz.yanghaoyu.flora.exception.BeansException;
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
        //

        // 1. 处理注解 @Value
        for (Field field : declaredFields) {
            Value valueAnnotation = field.getAnnotation(Value.class);
            if (null != valueAnnotation) {
                String value = valueAnnotation.value();
                String tempValue = beanFactory.resolveEmbeddedValue(value);
                if (tempValue == null && valueAnnotation.required()) {
                    throw new NullPointerException();
                }
                value = tempValue;
                ReflectUtil.setFieldValue(bean, field.getName(), value);
            }
        }


        for (Field field : declaredFields) {
            // 处理 Inject.ByType
            Inject.ByType injectByTypeAnno = field.getAnnotation(Inject.ByType.class);
            if (injectByTypeAnno != null) {
                Map<String, ?> candidates = beanFactory.getBeansOfType(field.getType());
                if (candidates.size() == 1) {
                    for (Object dependOnBean : candidates.values()) {
                        ReflectUtil.setFieldValue(bean, field.getName(), dependOnBean);
                    }
                } else {
                    if (injectByTypeAnno.require()) {
                        if (candidates.size() > 1) {
                            throw new BeansException("multiple beans are candidate! at: " + beanName + "field: " + field.getName() + ". Please use Inject.ByName");
                        } else {
                            throw new BeansException("No such Bean which class is  " + field.getType().getName());
                        }
                    }
                }
            }

            // 处理 Inject.ByName
            Inject.ByName injectByNameAnno = field.getAnnotation(Inject.ByName.class);
            if (injectByNameAnno != null) {
                String value = injectByNameAnno.value();
                String id = injectByNameAnno.id();
                if (StringUtil.isEmpty(id)) {
                    id = StringUtil.isEmpty(value)
                            ? StringUtil.lowerFirstChar(field.getName())
                            : value;
                }
                Object dependOnBean = beanFactory.getBean(id);
                if (dependOnBean == null && injectByNameAnno.require()) {
                    throw new BeansException("No such Bean which id is  " + id);
                }
                ReflectUtil.setFieldValue(bean, field.getName(), dependOnBean);
            }
        }

        return null;
    }
}
