package xyz.yanghaoyu.flora.core.beans.factory.support;

import cn.hutool.core.util.TypeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;
import xyz.yanghaoyu.flora.core.beans.factory.*;
import xyz.yanghaoyu.flora.core.beans.factory.config.*;
import xyz.yanghaoyu.flora.exception.BeanCreateException;
import xyz.yanghaoyu.flora.exception.BeansException;
import xyz.yanghaoyu.flora.util.ConversionUtil;
import xyz.yanghaoyu.flora.util.ReflectUtil;
import xyz.yanghaoyu.flora.util.StringUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
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

public abstract class AbstractAutowireCapableBeanFactory
        extends AbstractBeanFactory implements AutowireCapableBeanFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractAutowireCapableBeanFactory.class);

    // 实例化策略
    private final InstantiationStrategy instantiationStrategy = new JDKInstantiationStrategy();

    @Override
    protected Object createBean(String beanName, BeanDefinition beanDefinition, Object[] args) throws BeansException {
        // 判断是否返回代理 Bean 对象
        Object bean = resolveBeforeInstantiation(beanName, beanDefinition);
        if (null != bean) {
            return bean;
        }
        return doCreateBean(beanName, beanDefinition, args);
    }

    protected Object doCreateBean(String beanName, BeanDefinition beanDefinition, Object[] args) throws BeansException {
        LOGGER.trace("create [Bean] [{}]", beanName);
        Object bean = null;
        try {
            // 实例化
            addCurrentlyCreatingBean(beanName);

            bean = createBeanInstance(beanDefinition, beanName, args);

            // 先把 bean 暴露在三级缓存中, 解决循环依赖
            if (beanDefinition.isSingleton()) {
                Object finalBean = bean;
                addSingletonFactory(
                        beanName, () -> getEarlyBeanReference(beanName, beanDefinition, finalBean)
                );
            }

            // 实例化后, 返回 false 不再向下执行
            if (!applyBeanPostProcessorsAfterInstantiation(beanName, bean)) {
                return bean;
            }

            // 在设置 Bean 属性之前，允许 BeanPostProcessor 修改属性值
            applyBeanPostProcessorsBeforeApplyingPropertyValues(beanName, bean, beanDefinition);

            // 给 Bean 填充属性
            applyPropertyValues(beanName, bean, beanDefinition);

            // 执行 Bean 的初始化方法和 BeanPostProcessor 的前置和后置处理方法
            bean = initializeBean(beanName, bean, beanDefinition);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BeanCreateException("Instantiation of bean failed when creating bean [" + beanName + "]", e);
        }
        registerDisposableBeanIfNecessary(beanName, bean, beanDefinition);

        Object exposedObject = bean;
        // 注册进入单例容器
        if (beanDefinition.isSingleton()) {
            // 此时的 bean 有可能还不是代理对象
            // 先从容器中拿一下 确保获取到代理对象
            exposedObject = getSingleton(beanName);
            // 再注入容器
            registerSingleton(beanName, exposedObject);
        }
        removeCurrentlyCreatingBean(beanName);
        return exposedObject;
    }

    protected Object resolveBeforeInstantiation(String beanName, BeanDefinition beanDefinition) {
        Object bean = applyBeanPostProcessorsBeforeInstantiation(beanDefinition.getBeanClass(), beanName);
        // 如果被代理了 则直接执行 BeanPostProcessorsAfterInitialization
        if (bean != null) {
            bean = applyBeanPostProcessorsAfterInitialization(bean, beanName);
        }
        return bean;
    }

    protected Object getEarlyBeanReference(String beanName, BeanDefinition beanDefinition, Object bean) {
        Object exposedObject = bean;
        for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
            if (beanPostProcessor instanceof SmartInstantiationAwareBeanPostProcessor) {
                exposedObject = ((SmartInstantiationAwareBeanPostProcessor) beanPostProcessor).getEarlyBeanReference(exposedObject, beanName);
                if (exposedObject == null) {
                    return null;
                }
            }
        }
        return exposedObject;
    }

    /**
     * 在实例化之前
     */
    protected Object applyBeanPostProcessorsBeforeInstantiation(Class beanClass, String beanName) {
        for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
            if (beanPostProcessor instanceof InstantiationAwareBeanPostProcessor) {
                Object result = ((InstantiationAwareBeanPostProcessor) beanPostProcessor).postProcessBeforeInstantiation(beanClass, beanName);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }

    /**
     * 实例化
     */
    protected Object createBeanInstance(BeanDefinition beanDefinition, String beanName, Object[] args) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        if (beanDefinition.getFactoryMethod() != null) {
            String configurationClassBeanName = beanDefinition.getConfigurationClassBeanName();
            Object configProxyBean            = getBean(configurationClassBeanName);

            Method     factoryMethod  = beanDefinition.getFactoryMethod();
            Class<?>[] parameterTypes = factoryMethod.getParameterTypes();
            Method     method         = configProxyBean.getClass().getMethod(factoryMethod.getName(), parameterTypes);

            return method.invoke(configProxyBean, new Object[parameterTypes.length]);
        }

        Constructor constructor = null;
        try {
            constructor = ReflectUtil.selectConstructorByArgsType(beanDefinition.getBeanClass(), args);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            throw new BeanCreateException("Error creating bean instance " + beanName);
        }
        return instantiationStrategy.instantiate(beanDefinition, constructor, args);
    }

    /**
     * 在实例化之后
     * Bean 实例化后对于返回 false 的对象，不在执行后续设置 Bean 对象属性的操作
     */
    protected boolean applyBeanPostProcessorsAfterInstantiation(String beanName, Object bean) {
        for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
            if (beanPostProcessor instanceof InstantiationAwareBeanPostProcessor) {
                if (!((InstantiationAwareBeanPostProcessor) beanPostProcessor).postProcessAfterInstantiation(bean, beanName)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 在设置 Bean 属性之前，允许 BeanPostProcessor 修改属性值
     */
    protected void applyBeanPostProcessorsBeforeApplyingPropertyValues(String beanName, Object bean, BeanDefinition beanDefinition) {
        List<BeanPostProcessor> beanPostProcessors = getBeanPostProcessors();
        for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
            if (beanPostProcessor instanceof InstantiationAwareBeanPostProcessor) {
                // PropertyValues pvs =
                ((InstantiationAwareBeanPostProcessor) beanPostProcessor).postProcessPropertyValues(beanDefinition.getPropertyValues(), bean, beanName);
                // if (pvs != null) {
                //     for (PropertyValue propertyValue : pvs.getPropertyValues()) {
                //         beanDefinition.getPropertyValues().addPropertyValue(propertyValue);
                //     }
                // }
            }
        }
    }


    /**
     * 属性填充
     */
    protected void applyPropertyValues(String beanName, Object bean, BeanDefinition beanDefinition) {
        PropertyValues propertyValues = beanDefinition.getPropertyValues();
        try {
            for (PropertyValue propertyValue : propertyValues) {
                String name  = propertyValue.getName();
                Object value = propertyValue.getValue();
                if (value instanceof BeanReference) {
                    value = getBean(((BeanReference) value).getBeanName());
                }
                else {
                    Class<?> sourceType = value.getClass();
                    Class<?> targetType = null;
                    Type     fieldType  = TypeUtil.getFieldType(bean.getClass(), name);
                    if (fieldType instanceof ParameterizedTypeImpl) {
                        targetType = ((ParameterizedTypeImpl) fieldType).getRawType();
                    }
                    else {
                        targetType = ((Class<?>) fieldType);
                    }
                    ConversionUtil.convert(value, targetType, sourceType, ((ConfigurableListableBeanFactory) this).getConversionService());
                }
                ReflectUtil.setFieldValue(bean, name, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BeansException("Error setting property values：" + beanName);
        }
    }

    protected Object initializeBean(String beanName, Object bean, BeanDefinition beanDefinition) {
        // 处理与BeanFactory有关的容器感知对象,
        if (bean instanceof Aware) {
            if (bean instanceof BeanFactoryAware) {
                ((BeanFactoryAware) bean).setBeanFactory(this);
            }
            else if (bean instanceof BeanNameAware) {
                ((BeanNameAware) bean).setBeanName(beanName);
            }
            else if (bean instanceof BeanClassLoaderAware) {
                ((BeanClassLoaderAware) bean).setBeanClassLoader(bean.getClass().getClassLoader());
            }
            else if (bean instanceof BeanDefinitionRegistryAware) {
                ((BeanDefinitionRegistryAware) bean).setBeanRegistry((BeanDefinitionRegistry) this);
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

    /**
     * 在初始化之前
     */
    protected Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) throws BeansException {
        Object result = existingBean;
        for (BeanPostProcessor processor : getBeanPostProcessors()) {
            Object current = processor.postProcessBeforeInitialization(result, beanName);
            if (null == current) {
                return result;
            }
            result = current;
        }
        return result;
    }

    /**
     * 触发初始化方法
     */
    protected void invokeInitMethods(String beanName, Object wrappedBean, BeanDefinition beanDefinition) throws Exception {
        if (wrappedBean instanceof InitializingBean) {
            ((InitializingBean) wrappedBean).afterPropertiesSet();
        }
        // beanDefinition
        String initMethodName = beanDefinition.getInitMethodName();

        if (StringUtil.isNotEmpty(initMethodName)) {
            // 避免二次执行
            if (wrappedBean instanceof InitializingBean && Objects.equals("afterPropertiesSet", initMethodName)) {
                return;
            }
            Method initMethod = beanDefinition.getBeanClass().getMethod(initMethodName);
            initMethod.invoke(wrappedBean);
        }
    }

    /**
     * 在初始化之后
     */
    protected Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) throws BeansException {
        Object result = existingBean;
        for (BeanPostProcessor processor : getBeanPostProcessors()) {
            Object current = processor.postProcessAfterInitialization(result, beanName);
            if (null == current) {
                return result;
            }
            result = current;
        }
        return result;
    }

    /**
     * 注册销毁方法
     */
    protected void registerDisposableBeanIfNecessary(String beanName, Object bean, BeanDefinition beanDefinition) {
        if (!beanDefinition.isSingleton()) {
            return;
        }
        if (bean instanceof DisposableBean || StringUtil.isNotEmpty(beanDefinition.getDestroyMethodName())) {
            DisposableBeanAdapter adapter = new DisposableBeanAdapter(bean, beanName, beanDefinition);
            List<DestructionAwareBeanPostProcessor> destructionAwareBeanPostProcessors =
                    (List) getBeanPostProcessors()
                            .stream()
                            .filter(processor -> processor instanceof DestructionAwareBeanPostProcessor)
                            .collect(Collectors.toList());
            adapter.setBeanPostProcessors(destructionAwareBeanPostProcessors);
            registerDisposableBean(beanName, adapter);
        }
    }
}
