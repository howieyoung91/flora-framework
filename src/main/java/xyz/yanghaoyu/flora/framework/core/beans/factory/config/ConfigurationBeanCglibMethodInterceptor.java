/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.framework.core.beans.factory.config;

import cn.hutool.core.collection.ConcurrentHashSet;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.yanghaoyu.flora.framework.annotation.Bean;
import xyz.yanghaoyu.flora.framework.annotation.Component;
import xyz.yanghaoyu.flora.framework.annotation.Inject;
import xyz.yanghaoyu.flora.framework.core.beans.factory.ConfigurableListableBeanFactory;
import xyz.yanghaoyu.flora.framework.core.beans.factory.support.DefaultListableBeanFactory;
import xyz.yanghaoyu.flora.framework.exception.BeanCandidatesException;
import xyz.yanghaoyu.flora.framework.exception.DuplicateDeclarationException;
import xyz.yanghaoyu.flora.framework.util.ComponentUtil;
import xyz.yanghaoyu.flora.framework.util.StringUtil;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.Set;

public class ConfigurationBeanCglibMethodInterceptor implements MethodInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationBeanCglibMethodInterceptor.class);

    private final DefaultListableBeanFactory beanFactory;
    private final Set<Method>                cache = new ConcurrentHashSet<>();

    public ConfigurationBeanCglibMethodInterceptor(ConfigurableListableBeanFactory beanFactory) {
        this.beanFactory = (DefaultListableBeanFactory) beanFactory;
    }

    /**
     * 拦截 factoryMethod，先从 beanFactory 中获取
     */
    @Override
    public Object intercept(Object target, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        if (!cache.contains(method)) {
            if (method.isAnnotationPresent(Bean.class)) {
                cache.add(method);
            }
        }
        if (cache.contains(method)) {
            String beanName = ComponentUtil.determineBeanName(method, method.getAnnotation(Bean.class));
            if (!beanFactory.isCreatingCurrently(beanName)) {
                return beanFactory.getBean(beanName);
            }

            // 解析参数, 注入
            resolveFactoryMethod(method, args, beanName);
        }
        return methodProxy.invokeSuper(target, args);
    }

    private void resolveFactoryMethod(Method method, Object[] args, String beanName) {
        int i = 0;

        Parameter[] parameters = method.getParameters();
        for (Parameter parameter : parameters) {
            Inject.ByName byNameAnn = parameter.getAnnotation(Inject.ByName.class);
            Inject.ByType byTypeAnn = parameter.getAnnotation(Inject.ByType.class);
            // check
            if (byNameAnn != null && byTypeAnn != null) {
                throw new DuplicateDeclarationException("duplicate declaration [@Inject.ByName],[@Inject.ByType] on " + parameter.getName() + " when creating bean [" + beanName + "]");
            }
            Object bean = findDependOnBeanFromBeanFactory(beanName, parameter, byNameAnn, byTypeAnn);
            args[i] = bean;
            i++;
        }
    }

    /**
     * 从 beanFactory 中找到依赖的 bean，默认 ByName
     */
    private Object findDependOnBeanFromBeanFactory(String beanName, Parameter parameter, Inject.ByName byNameAnn, Inject.ByType byTypeAnn) {
        Object bean = null;
        if (byTypeAnn != null) {
            Class dependOnBeanClass = determineDependOnBeanClass(parameter, byTypeAnn);
            // FIXME 依赖于和 Bean 相同的类型 这里就会出现循环依赖
            Map<String, ?> candidate = beanFactory.getBeansOfType(dependOnBeanClass);
            if (candidate.size() == 0) {
                if (!byTypeAnn.required()) {
                    return null;
                }
                throw new BeanCandidatesException("find no candidate when creating bean [" + beanName + "]");
            }
            if (candidate.size() > 1) {
                throw new BeanCandidatesException("find too many candidates whose class is " + dependOnBeanClass + " when creating bean [" + beanName + "]");
            }
            bean = candidate.values().iterator().next();
        }
        else {
            // byName
            String  dependOnBeanName = Component.DEFAULT_BEAN_NAME;
            boolean required         = true;
            if (byNameAnn == null) {
                dependOnBeanName = parameter.getName();
            }
            else {
                dependOnBeanName = determineDependOnBeanName(parameter, byNameAnn);
                required = byNameAnn.required();
            }

            if (beanFactory.containsBeanDefinition(dependOnBeanName)) {
                bean = beanFactory.getBean(dependOnBeanName);
            }
            else {
                if (required) {
                    throw new BeanCandidatesException("find no candidate [" + dependOnBeanName + "] when creating bean [" + beanName + "]");
                }
            }

        }
        return bean;
    }

    private static String determineDependOnBeanName(Parameter parameter, Inject.ByName byNameAnn) {
        String dependOnBeanName;
        String value = byNameAnn.value();
        dependOnBeanName = byNameAnn.name();
        if (StringUtil.isEmpty(dependOnBeanName)) {
            dependOnBeanName = StringUtil.isEmpty(value)
                    ? StringUtil.lowerFirstChar(parameter.getName())
                    : value;
        }
        return dependOnBeanName;
    }

    private static Class determineDependOnBeanClass(Parameter parameter, Inject.ByType byTypeAnn) {
        Class value = byTypeAnn.value();
        Class clazz = byTypeAnn.clazz();

        Class dependOnBeanClass = value == clazz
                ? value
                : clazz == Inject.ByType.DEFAULT_CLASS ? value : clazz;

        if (dependOnBeanClass == Inject.ByType.DEFAULT_CLASS) {
            dependOnBeanClass = parameter.getType();
        }
        return dependOnBeanClass;
    }
}