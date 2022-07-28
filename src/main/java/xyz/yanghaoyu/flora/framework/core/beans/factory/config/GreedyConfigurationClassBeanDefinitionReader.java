/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.framework.core.beans.factory.config;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import xyz.yanghaoyu.flora.framework.annotation.Bean;
import xyz.yanghaoyu.flora.framework.core.beans.factory.support.DefaultListableBeanFactory;
import xyz.yanghaoyu.flora.framework.exception.BeansException;
import xyz.yanghaoyu.flora.framework.annotation.Conditional;
import xyz.yanghaoyu.flora.framework.util.ComponentUtil;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * 对 @Conditional 进行支持，总是尽可能地保证 bean 被注入
 *
 * @see Conditional
 */
public final class GreedyConfigurationClassBeanDefinitionReader {
    private DefaultListableBeanFactory beanFactory;

    public GreedyConfigurationClassBeanDefinitionReader(DefaultListableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public Set<String> loadBeanDefinitions(Set<ConfigurationClass> configurationClasses) {
        Set<String> skippedBeanNames = new HashSet<>();
        for (ConfigurationClass configurationClass : configurationClasses) {
            String         configBeanName = configurationClass.getBeanName();
            BeanDefinition configBeanDef  = beanFactory.getBeanDefinition(configBeanName);

            proxyConfigBeanClass(configBeanDef);

            registerBeanFactoryMethods(configBeanName, configurationClass.getBeanFactoryMethods());
            // register skipped bean definition
            for (Method factoryMethod : configurationClass.getSkippedBeanFactoryMethods()) {
                String skippedBeanName = register(configBeanName, factoryMethod);
                skippedBeanNames.add(skippedBeanName);
            }
        }
        return skippedBeanNames;
    }

    private void registerBeanFactoryMethods(String configBeanName, Set<Method> factoryMethods) {
        for (Method factoryMethod : factoryMethods) {
            register(configBeanName, factoryMethod);
        }
    }

    private void proxyConfigBeanClass(BeanDefinition configBeanDef) {
        Class    configBeanClass         = configBeanDef.getBeanClass();
        Class<?> configurationProxyClass = getProxyClass(beanFactory, configBeanClass);
        configBeanDef.setBeanClass(configurationProxyClass);
    }

    private String register(String configBeanName, Method factoryMethod) {
        BeanDefinition beanDef  = buildBeanDefinition(configBeanName, factoryMethod);
        Bean           beanAnn  = factoryMethod.getAnnotation(Bean.class);
        String         beanName = ComponentUtil.determineBeanName(factoryMethod, beanAnn);
        register(beanName, beanDef);
        return beanName;
    }

    private void register(String beanName, BeanDefinition beanDef) {
        if (beanFactory.containsBeanDefinition(beanName)) {
            throw new BeansException("Duplicate beanName [" + beanName + "] is not allowed");
        }
        beanFactory.registerBeanDefinition(beanName, beanDef);
    }

    private static BeanDefinition buildBeanDefinition(String configBeanName, Method method) {
        Class<?>       beanClass = method.getReturnType();
        BeanDefinition beanDef   = new BeanDefinition(beanClass);
        beanDef.setConfigurationClassBeanName(configBeanName);
        beanDef.setFactoryMethod(method);
        ComponentUtil.determineBeanScope(method, beanDef);
        return beanDef;
    }

    private static Class<?> getProxyClass(DefaultListableBeanFactory beanFactory, Class<?> configBeanClass) {
        return getProxyClassUsingCglib(beanFactory, configBeanClass);
    }

    private static Class getProxyClassUsingCglib(DefaultListableBeanFactory beanFactory, Class<?> configBeanClass) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(configBeanClass);
        enhancer.setInterfaces(configBeanClass.getInterfaces());
        enhancer.setCallbackType(ConfigurationBeanCglibMethodInterceptor.class);
        Class enhancedClass = enhancer.createClass();
        Enhancer.registerStaticCallbacks(enhancedClass, new Callback[]{new ConfigurationBeanCglibMethodInterceptor(beanFactory)});
        return enhancedClass;
    }
}
