package xyz.yanghaoyu.flora.beans.factory.support;

import xyz.yanghaoyu.flora.BeansException;
import xyz.yanghaoyu.flora.beans.factory.*;
import xyz.yanghaoyu.flora.beans.factory.config.AutowireCapableBeanFactory;
import xyz.yanghaoyu.flora.beans.factory.config.BeanDefinition;
import xyz.yanghaoyu.flora.beans.factory.config.BeanPostProcessor;
import xyz.yanghaoyu.flora.beans.factory.config.BeanReference;
import xyz.yanghaoyu.flora.util.ReflectUtil;
import xyz.yanghaoyu.flora.util.StringUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * <h1>
 * 对实例化bean,自动注入提供支持
 * </h1>
 * <pre style="color:orange;font-size:13px">
 * 主要流程:
 * ① createBeanInstance
 * ② applyPropertyValues
 * ③ applyBeanPostProcessorsBeforeInitialization
 * ④ initMethod
 * ⑤ applyBeanPostProcessorsAfterInitialization
 * </pre>
 *
 * @author <a href="https://www.yanghaoyu.xyz">Howie Young</a><i>on 2021/8/7 20:46<i/>
 * @version 1.0
 */

public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory implements AutowireCapableBeanFactory {
    private InstantiationStrategy instantiationStrategy = new JDKInstantiationStrategy();

    @Override
    protected Object createBean(String beanName, BeanDefinition beanDefinition, Object[] args) throws BeansException {
        Object bean = null;
        try {
            bean = createBeanInstance(beanDefinition, beanName, args);
            // 给 Bean 填充属性
            applyPropertyValues(beanName, bean, beanDefinition);
            // 执行 Bean 的初始化方法和 BeanPostProcessor 的前置和后置处理方法
            bean = initializeBean(beanName, bean, beanDefinition);
            registerDisposableBeanIfNecessary(beanName, bean, beanDefinition);

        } catch (Exception e) {
            throw new BeansException("Instantiation of bean failed", e);
        }
        if (beanDefinition.isSingleton()) {
            registerSingleton(beanName, bean);
        }
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

    private Object initializeBean(String beanName, Object bean, BeanDefinition beanDefinition) {
        // 处理与BeanFactory有关的容器感知对象
        if (bean instanceof Aware) {
            if (bean instanceof BeanFactoryAware) {
                ((BeanFactoryAware) bean).setBeanFactory(this);
            } else if (bean instanceof BeanNameAware) {
                ((BeanNameAware) bean).setBeanName(beanName);
            } else if (bean instanceof BeanClassLoaderAware) {
                ((BeanClassLoaderAware) bean).setBeanClassLoader(bean.getClass().getClassLoader());
            }
        }

        // 1. 执行 BeanPostProcessor Before 处理
        Object wrappedBean = applyBeanPostProcessorsBeforeInitialization(bean, beanName);

        try {
            invokeInitMethods(beanName, wrappedBean, beanDefinition);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 2. 执行 BeanPostProcessor After 处理
        wrappedBean = applyBeanPostProcessorsAfterInitialization(bean, beanName);
        return wrappedBean;
    }

    private void invokeInitMethods(String beanName, Object wrappedBean, BeanDefinition beanDefinition) throws Exception {
        // 通过实现接口
        if (wrappedBean instanceof InitializingBean) {
            ((InitializingBean) wrappedBean).afterPropertiesSet();
        }
        // beanDefinition
        String initMethodName = beanDefinition.getInitMethodName();
        // initMethodName 不为空
        if (StringUtil.isNotEmpty(initMethodName)) {
            // 避免二次执行
            if (wrappedBean instanceof InitializingBean && Objects.equals("afterPropertiesSet", initMethodName)) {
                return;
            }
            Method initMethod = beanDefinition.getBeanClass().getMethod(initMethodName);
            initMethod.invoke(wrappedBean);
        }

    }

    @Override
    public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) throws BeansException {
        Object result = existingBean;
        for (BeanPostProcessor processor : getBeanPostProcessors()) {
            Object current = processor.postProcessBeforeInitialization(result, beanName);
            if (null == current) return result;
            result = current;
        }
        return result;
    }

    @Override
    public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) throws BeansException {
        Object result = existingBean;
        for (BeanPostProcessor processor : getBeanPostProcessors()) {
            Object current = processor.postProcessAfterInitialization(result, beanName);
            if (null == current) return result;
            result = current;
        }
        return result;
    }

    protected void registerDisposableBeanIfNecessary(String beanName, Object bean, BeanDefinition beanDefinition) {
        if (!beanDefinition.isSingleton()) return;
        if (bean instanceof DisposableBean || StringUtil.isNotEmpty(beanDefinition.getDestroyMethodName())) {
            registerDisposableBean(beanName, new DisposableBeanAdapter(bean, beanName, beanDefinition));
        }
    }
}
