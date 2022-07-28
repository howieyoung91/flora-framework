/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.framework.core.beans.factory.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.yanghaoyu.flora.framework.annotation.Bean;
import xyz.yanghaoyu.flora.framework.annotation.Conditional;
import xyz.yanghaoyu.flora.framework.annotation.Configuration;
import xyz.yanghaoyu.flora.framework.core.beans.factory.support.DefaultListableBeanFactory;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * 这个类用于解析被 @Configuration 标记的类
 * 并把这些类全部注册进入 beanFactory
 *
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/12/27 12:09<i/>
 * @version 1.0
 */

public class ConfigurationBeanClassParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationBeanClassParser.class);

    private final Set<String>                configBeanNames;
    private final DefaultListableBeanFactory beanFactory;

    public ConfigurationBeanClassParser(DefaultListableBeanFactory beanFactory, Set<String> configBeanName) {
        this.configBeanNames = configBeanName;
        this.beanFactory = beanFactory;
    }

    public Set<ConfigurationClass> parse() {
        Set<ConfigurationClass> configurationClasses = new HashSet<>();

        for (String configBeanName : configBeanNames) {
            LOGGER.trace("parse [Configuration] [{}] ...", configBeanName);
            ConfigurationClass configurationClass = new ConfigurationClass(configBeanName);
            configurationClasses.add(configurationClass);

            BeanDefinition configBeanDef   = beanFactory.getBeanDefinition(configBeanName);
            Class<?>       configBeanClass = configBeanDef.getBeanClass();
            Configuration  configAnn       = configBeanClass.getAnnotation(Configuration.class);
            if (configAnn == null) {
                continue;
            }

            for (Method method : configBeanClass.getMethods()) {
                Bean beanAnn = method.getAnnotation(Bean.class);

                if (beanAnn == null) {
                    continue;
                }

                if (shouldSkip(method)) {
                    configurationClass.addSkippedBeanFactoryMethod(method);
                }
                else {
                    configurationClass.addBeanFactoryMethod(method);
                }
            }
        }
        return configurationClasses;
    }

    private static boolean shouldSkip(Method factoryMethod) {
        return factoryMethod.isAnnotationPresent(Conditional.class)
               || factoryMethod.isAnnotationPresent(Conditional.OnBean.class)
               || factoryMethod.isAnnotationPresent(Conditional.OnMissingBean.class)
               || factoryMethod.isAnnotationPresent(Conditional.OnProperty.class)
               || factoryMethod.isAnnotationPresent(Conditional.OnClass.class)
               || factoryMethod.isAnnotationPresent(Conditional.OnMissingClass.class);
    }
}
