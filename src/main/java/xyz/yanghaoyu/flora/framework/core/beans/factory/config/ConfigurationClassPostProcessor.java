/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.framework.core.beans.factory.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.yanghaoyu.flora.framework.annotation.Configuration;
import xyz.yanghaoyu.flora.framework.core.beans.factory.PropertyValue;
import xyz.yanghaoyu.flora.framework.core.beans.factory.PropertyValues;
import xyz.yanghaoyu.flora.framework.core.beans.factory.support.BeanDefinitionRegistry;
import xyz.yanghaoyu.flora.framework.core.beans.factory.support.DefaultListableBeanFactory;
import xyz.yanghaoyu.flora.framework.exception.BeansException;
import xyz.yanghaoyu.flora.framework.util.BeanUtil;

import java.util.HashSet;
import java.util.Set;

/**
 * 对 @Configuration @Bean 做支持
 *
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/12/4 22:57<i/>
 * @version 1.0
 */

public class ConfigurationClassPostProcessor implements BeanDefinitionRegistryPostProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationClassPostProcessor.class);

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry configBeanFactory) throws BeansException {
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) configBeanFactory;
        HashSet<String>            classes     = findConfigBeanName(beanFactory);

        LOGGER.trace("scan [Configuration] from {} ...", classes);
        ConfigurationBeanClassScanner scanner = new ConfigurationBeanClassScanner(beanFactory, classes);

        Set<String> configBeanNames = scanner.scan();

        LOGGER.trace("found [Configuration] -> {}", configBeanNames);
        ConfigurationBeanClassParser parser = new ConfigurationBeanClassParser(beanFactory, configBeanNames);

        Set<ConfigurationClass> configurationClasses = parser.parse();

        LOGGER.trace("loading configurations...");
        GreedyConfigurationClassBeanDefinitionReader reader = new GreedyConfigurationClassBeanDefinitionReader(beanFactory);

        // load
        Set<String> skippedStringNames = reader.loadBeanDefinitions(configurationClasses);

        registerConditionalSupportBeanFactoryPostProcessor(beanFactory, skippedStringNames);
    }

    private void registerConditionalSupportBeanFactoryPostProcessor(DefaultListableBeanFactory beanFactory, Set<String> skippedStringNames) {
        PropertyValues propertyValues = new PropertyValues()
                .addPropertyValue(new PropertyValue("skippedBeanNames", skippedStringNames));
        BeanDefinition beanDef = new BeanDefinition(ConditionalSupportPostProcessor.class, propertyValues);
        beanFactory.registerBeanDefinition(BeanUtil.builtInBeanName(ConditionalSupportPostProcessor.class), beanDef);
    }

    private HashSet<String> findConfigBeanName(DefaultListableBeanFactory beanFactory) {
        String[]        beanNames = beanFactory.getBeanDefinitionNames();
        HashSet<String> classes   = new HashSet<>();
        for (String beanName : beanNames) {
            BeanDefinition configBeanDef   = beanFactory.getBeanDefinition(beanName);
            Class<?>       configBeanClass = configBeanDef.getBeanClass();
            if (!configBeanClass.isAnnotationPresent(Configuration.class)) {
                continue;
            }
            classes.add(beanName);
        }
        return classes;
    }
}
