package xyz.yanghaoyu.flora.core.beans.factory.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.yanghaoyu.flora.annotation.Inject;
import xyz.yanghaoyu.flora.annotation.Value;
import xyz.yanghaoyu.flora.core.Ordered;
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

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Support @Inject.ByType @Inject.ByName @Value
 */
public class AutowiredAnnotationBeanPostProcessor
        implements InstantiationAwareBeanPostProcessor, BeanFactoryAware, Ordered {
    private static final Logger LOGGER = LoggerFactory.getLogger(AutowiredAnnotationBeanPostProcessor.class);

    private ConfigurableListableBeanFactory beanFactory;

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE / 2 + 1;
    }

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
        // handleConfigurationPropertiesAnnotation(bean, clazz);
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
            Class dependOnBeanClass = determineDependOnBeanClass(field, injectByTypeAnno);

            // select a candidate
            Map<String, ?> candidates = beanFactory.getBeansOfType(dependOnBeanClass);
            if (candidates.size() == 1) {
                for (Object dependOnBean : candidates.values()) {
                    ReflectUtil.setFieldValue(bean, actualClass, field.getName(), dependOnBean);
                }
            }
            else {
                if (injectByTypeAnno.required()) {
                    if (candidates.size() > 1) {
                        throw new BeansException("multiple beans are candidate! at: " + beanName + "field: " + field.getName() + ". Please use Inject.ByName");
                    }
                    else {
                        throw new BeansException("No such Bean which class is  " + field.getType().getName() + " when gen " + beanName);
                    }
                }
            }
        }

        // 处理 Inject.ByName
        if (injectByNameAnno != null) {
            String value = injectByNameAnno.value();
            String id    = injectByNameAnno.name();
            if (StringUtil.isEmpty(id)) {
                id = StringUtil.isEmpty(value) ? StringUtil.lowerFirstChar(field.getName()) : value;
            }
            Object dependOnBean = null;
            try {
                dependOnBean = beanFactory.getBean(id);
            }
            catch (Exception e) {
                LOGGER.info("{}", e.toString());
                if (injectByNameAnno.required()) {
                    throw new BeansException("no such bean named " + id);
                }
            }
            ReflectUtil.setFieldValue(bean, actualClass, field.getName(), dependOnBean);
        }
    }

    private Class determineDependOnBeanClass(Field field, Inject.ByType injectByTypeAnno) {
        Class value = injectByTypeAnno.value();
        Class clazz = injectByTypeAnno.clazz();

        Class dependOnBeanClass = value == clazz
                ? value
                : clazz == Inject.ByType.DEFAULT_CLASS ? value : clazz;

        if (dependOnBeanClass == Inject.ByType.DEFAULT_CLASS) {
            dependOnBeanClass = field.getType();
        }
        return dependOnBeanClass;
    }

    private void handleValueAnnotation(Object bean, Field field, Class<?> clazz) {
        Value valueAnn = field.getAnnotation(Value.class);
        if (valueAnn != null) {
            String key   = valueAnn.value();
            String value = beanFactory.resolveEmbeddedValue(key);

            if (StringUtil.isEmpty(value) ||
                (PropertyUtil.isPropertyKey(key) &&
                 PropertyUtil.isPropertyKey(value))
            ) {
                if (valueAnn.required()) {
                    throw new BeansException("Fail to find the value [" + valueAnn.value() + "]");
                }
                value = null;
                ReflectUtil.setFieldValue(bean, clazz, field.getName(), null);
            }
            else {
                // 类型转换
                Object v = ConversionUtil.convertField(field, value, beanFactory.getConversionService());
                ReflectUtil.setFieldValue(bean, clazz, field.getName(), v);
            }
        }
    }
}

