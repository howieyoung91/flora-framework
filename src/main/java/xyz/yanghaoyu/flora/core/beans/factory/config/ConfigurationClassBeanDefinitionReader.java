/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.core.beans.factory.config;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import xyz.yanghaoyu.flora.annotation.Bean;
import xyz.yanghaoyu.flora.annotation.ConditionContext;
import xyz.yanghaoyu.flora.annotation.condition.FloraConditionChain;
import xyz.yanghaoyu.flora.core.beans.factory.ConfigurableListableBeanFactory;
import xyz.yanghaoyu.flora.core.beans.factory.support.BeanDefinitionRegistry;
import xyz.yanghaoyu.flora.core.beans.factory.support.DefaultListableBeanFactory;
import xyz.yanghaoyu.flora.core.io.ResourceLoader;
import xyz.yanghaoyu.flora.core.io.loader.DefaultResourceLoader;
import xyz.yanghaoyu.flora.exception.BeansException;
import xyz.yanghaoyu.flora.util.ComponentUtil;
import xyz.yanghaoyu.flora.util.ReflectUtil;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Set;

public class ConfigurationClassBeanDefinitionReader {
    private DefaultListableBeanFactory beanFactory;
    private ConditionContext           conditionContext;
    private FloraConditionChain        conditionChain = new FloraConditionChain();

    public ConfigurationClassBeanDefinitionReader(DefaultListableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
        this.conditionContext = new InnerConditionContext(beanFactory, new DefaultResourceLoader());
    }


    public void loadBeanDefinitions(Set<ConfigurationClass> configurationClasses) {
        loadCommonBeanDefinitions(configurationClasses);
        loadSkippedBeanDefinitions(configurationClasses);
    }

    private void loadCommonBeanDefinitions(Set<ConfigurationClass> configurationClasses) {
        for (ConfigurationClass configurationClass : configurationClasses) {
            BeanDefinition configBeanDef = beanFactory.getBeanDefinition(configurationClass.getBeanName());

            // 使用 cglib 生成代理 config bean def 先从 beanFactory 中获取 bean
            proxyConfigBeanClass(configBeanDef);

            for (Method factoryMethod : configurationClass.getBeanFactoryMethods()) {
                BeanDefinition beanDef = buildBeanDefinition(configurationClass.getBeanName(), factoryMethod);

                Bean   beanAnn  = factoryMethod.getAnnotation(Bean.class);
                String beanName = ComponentUtil.determineBeanName(factoryMethod, beanAnn);
                register(beanName, beanDef);
            }
        }
    }

    private void loadSkippedBeanDefinitions(Set<ConfigurationClass> configurationClasses) {
        boolean shouldContinue = false;
        // Set<BeanDefinition> willRegisterBeanDefinitions = new HashSet<>();
        while (true) {
            for (ConfigurationClass configurationClass : configurationClasses) {
                Iterator<Method> iterator = configurationClass.getSkippedBeanFactoryMethods().iterator();
                while (iterator.hasNext()) {
                    Method         factoryMethod = iterator.next();
                    BeanDefinition beanDef       = buildBeanDefinition(configurationClass.getBeanName(), factoryMethod);
                    if (conditionChain.matches(conditionContext, beanDef)) {
                        Bean   beanAnn  = factoryMethod.getAnnotation(Bean.class);
                        String beanName = ComponentUtil.determineBeanName(factoryMethod, beanAnn);
                        register(beanName, beanDef);
                        iterator.remove();
                        shouldContinue = true;
                    }
                }
            }
            if (!shouldContinue) {
                break;
            }
            shouldContinue = false;
        }

        // while (true) {
        //     Iterator<BeanDefinition> iterator = willRegisterBeanDefinitions.iterator();
        //     while (iterator.hasNext()) {
        //         BeanDefinition beanDef = iterator.next();
        //         if (!conditionChain.matches(conditionContext, beanDef)) {
        //             iterator.remove();
        //             shouldContinue = true;
        //         }
        //     }
        //
        //     if (!shouldContinue) {
        //         break;
        //     }
        //     shouldContinue = false;
        // }
        //
        //
        // for (BeanDefinition beanDef : willRegisterBeanDefinitions) {
        //     Method factoryMethod = beanDef.getFactoryMethod();
        //     Bean   beanAnn       = factoryMethod.getAnnotation(Bean.class);
        //     String beanName      = ComponentUtil.determineBeanName(factoryMethod, beanAnn);
        //     register(beanName, beanDef);
        // }
    }

    private void proxyConfigBeanClass(BeanDefinition configBeanDef) {
        Class    configBeanClass         = configBeanDef.getBeanClass();
        Class<?> configurationProxyClass = getProxyClass(beanFactory, configBeanClass);
        configBeanDef.setBeanClass(configurationProxyClass);
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

    private static class InnerConditionContext implements ConditionContext {
        private DefaultListableBeanFactory beanFactory;
        private DefaultResourceLoader      resourceLoader = new DefaultResourceLoader();

        public InnerConditionContext(DefaultListableBeanFactory beanFactory, DefaultResourceLoader resourceLoader) {
            this.beanFactory = beanFactory;
            this.resourceLoader = resourceLoader;
        }

        @Override
        public BeanDefinitionRegistry getRegistry() {
            return beanFactory;
        }

        @Override
        public ConfigurableListableBeanFactory getBeanFactory() {
            return beanFactory;
        }

        @Override
        public ResourceLoader getResourceLoader() {
            return resourceLoader;
        }

        @Override
        public ClassLoader getClassLoader() {
            return ReflectUtil.getDefaultClassLoader();
        }
    }
}
