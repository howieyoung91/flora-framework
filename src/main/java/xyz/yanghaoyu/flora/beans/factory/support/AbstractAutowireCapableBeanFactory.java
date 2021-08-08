package xyz.yanghaoyu.flora.beans.factory.support;

import xyz.yanghaoyu.flora.BeansException;
import xyz.yanghaoyu.flora.beans.factory.PropertyValue;
import xyz.yanghaoyu.flora.beans.factory.PropertyValues;
import xyz.yanghaoyu.flora.beans.factory.config.BeanDefinition;
import xyz.yanghaoyu.flora.beans.factory.config.BeanReference;
import xyz.yanghaoyu.flora.util.ReflectUtil;

import java.lang.reflect.Constructor;

/**
 * @author <a href="https://www.yanghaoyu.xyz">Howie Young</a><i>on 2021/8/7 20:46<i/>
 * @version 1.0
 */

public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory {
    private InstantiationStrategy instantiationStrategy = new JDKInstantiationStrategy();

    @Override
    protected Object createBean(String beanName, BeanDefinition beanDefinition, Object[] args) throws BeansException {
        Object bean = createBeanInstance(beanDefinition, beanName, args);
        addSingleton(beanName, bean);
        applyPropertyValues(beanName, bean, beanDefinition);
        return bean;
    }

    protected Object createBeanInstance(BeanDefinition beanDefinition, String beanName, Object[] args) {
        Constructor ctorToUse = null;
        try {
            ctorToUse = ReflectUtil.selectCtorByArgsType(beanDefinition.getBeanClass(), args);
        } catch (NoSuchMethodException e) {
            throw new BeansException("Error creating bean instance " + beanName);
        }
        return instantiationStrategy.instantiate(beanDefinition, ctorToUse, args);
    }

    /**
     * 属性填充
     */
    protected void applyPropertyValues(String beanName, Object bean, BeanDefinition beanDefinition) {
        PropertyValues propertyValues = beanDefinition.getPropertyValues();
        try {
            for (PropertyValue propertyValue : propertyValues) {
                String name = propertyValue.getName();
                Object value = propertyValue.getValue();
                if (value instanceof BeanReference) {
                    value = getBean(((BeanReference) value).getBeanName());
                }
                ReflectUtil.setFieldValue(bean, name, value);
            }
        } catch (Exception e) {
            throw new BeansException("Error setting property values：" + beanName);
        }

    }
}
