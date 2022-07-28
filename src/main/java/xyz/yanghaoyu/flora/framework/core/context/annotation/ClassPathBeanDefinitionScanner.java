/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.framework.core.context.annotation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.yanghaoyu.flora.framework.core.beans.factory.config.BeanDefinition;
import xyz.yanghaoyu.flora.framework.util.ComponentUtil;

import java.util.HashSet;
import java.util.Set;

public class ClassPathBeanDefinitionScanner extends ClassPathScanningCandidateComponentProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClassPathBeanDefinitionScanner.class);

    public Set<BeanDefinition> scan(String... basePackages) {
        Set<BeanDefinition> beanDefinitions = new HashSet<>();
        for (String basePackage : basePackages) {
            LOGGER.trace("scan [Package] [{}]", basePackage);
            Set<Class<?>> candidateClasses = findCandidateComponents(basePackage);
            for (Class<?> candidateClass : candidateClasses) {
                BeanDefinition beanDefinition = new BeanDefinition(candidateClass);
                ComponentUtil.determineBeanScope(beanDefinition);
                beanDefinitions.add(beanDefinition);
            }
        }
        return beanDefinitions;
    }
}
