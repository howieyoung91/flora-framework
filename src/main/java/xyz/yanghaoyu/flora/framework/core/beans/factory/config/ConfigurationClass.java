/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.framework.core.beans.factory.config;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public final class ConfigurationClass {
    private       String      beanName;
    private final Set<Method> beanFactoryMethods        = new LinkedHashSet<>();
    private final Set<Method> skippedBeanFactoryMethods = new HashSet<>();

    public ConfigurationClass(String beanName) {
        this.beanName = beanName;
    }

    public String getBeanName() {
        return beanName;
    }

    public void addBeanFactoryMethod(Method method) {
        beanFactoryMethods.add(method);
    }

    public Set<Method> getBeanFactoryMethods() {
        return beanFactoryMethods;
    }

    public void addSkippedBeanFactoryMethod(Method method) {
        skippedBeanFactoryMethods.add(method);
    }

    public Set<Method> getSkippedBeanFactoryMethods() {
        return skippedBeanFactoryMethods;
    }
}
