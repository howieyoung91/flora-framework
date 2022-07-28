/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.framework.core.beans.factory.support;

import cn.hutool.core.collection.ConcurrentHashSet;
import xyz.yanghaoyu.flora.framework.core.OrderComparator;
import xyz.yanghaoyu.flora.framework.core.beans.factory.FactoryBean;
import xyz.yanghaoyu.flora.framework.core.beans.factory.StringValueResolver;
import xyz.yanghaoyu.flora.framework.core.beans.factory.config.BeanDefinition;
import xyz.yanghaoyu.flora.framework.core.beans.factory.config.BeanPostProcessor;
import xyz.yanghaoyu.flora.framework.core.beans.factory.config.ConfigurableBeanFactory;
import xyz.yanghaoyu.flora.framework.core.convert.converter.ConversionService;
import xyz.yanghaoyu.flora.framework.exception.BeansException;
import xyz.yanghaoyu.flora.framework.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 基本实现 BeanFactory 的流程
 * 具体逻辑由子类实现
 */
public abstract class AbstractBeanFactory
        extends FactoryBeanRegistrySupport implements ConfigurableBeanFactory {
    private final   Set<String>               currentlyCreatingBeans = new ConcurrentHashSet<>();
    private final   List<BeanPostProcessor>   beanPostProcessors     = new ArrayList<>();
    protected final List<StringValueResolver> embeddedValueResolvers = new CopyOnWriteArrayList<>();
    private         ConversionService         conversionService;

    @Override
    public void setConversionService(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public ConversionService getConversionService() {
        return conversionService;
    }

    protected <T> T doGetBean(final String name, final Object[] args) {

        String beanName = determineBeanName(name);

        Object sharedInstance = getSingleton(beanName);

        // 处理 FactoryBean
        if (sharedInstance != null) {
            return (T) getObjectForBeanInstance(sharedInstance, name);
        }

        BeanDefinition beanDefinition = getBeanDefinition(name);

        Object bean = createBean(name, beanDefinition, args);

        return (T) getObjectForBeanInstance(bean, name);
    }

    private String determineBeanName(String name) {
        if (!StringUtil.hasLength(name)) {
            return "";
        }
        if (name.charAt(0) == '&') {
            // &beanName
            return name.substring(1);
        }
        return name;
    }

    @Override
    public boolean containsBean(String name) {
        return containsBeanDefinition(name);
    }

    // 交给子类实现
    protected abstract boolean containsBeanDefinition(String name);

    private Object getObjectForBeanInstance(Object beanInstance, String beanName) {
        // 如果不是 FactoryBean,直接返回原对象即可
        // &beanName 表示要获取 FactoryBean 对象 那就不调用 FactoryBean#getObject 了
        if (!(beanInstance instanceof FactoryBean) || beanName.charAt(0) == '&') {
            return beanInstance;
        }

        FactoryBean<?> factoryBean = (FactoryBean<?>) beanInstance;

        return getObjectFromFactoryBean(factoryBean, beanName);
    }

    @Override
    public void addEmbeddedValueResolver(StringValueResolver valueResolver) {
        embeddedValueResolvers.add(valueResolver);
    }

    @Override
    public boolean hasEmbeddedValueResolver() {
        return !embeddedValueResolvers.isEmpty();
    }

    @Override
    public String resolveEmbeddedValue(String value) {
        if (value == null) {
            return null;
        }

        String result = value;
        for (StringValueResolver resolver : embeddedValueResolvers) {
            result = resolver.resolveStringValue(result);
        }
        return result;
    }

    @Override
    public Object getBean(String name) throws BeansException {
        return doGetBean(name, null);
    }

    @Override
    public Object getBean(String name, Object... args) throws BeansException {
        return doGetBean(name, args);
    }

    @Override
    public <T> T getBean(Class<T> type) {
        return doGetBean(StringUtil.lowerFirstChar(type.getSimpleName()), null);
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) {
        return ((T) getBean(name));
    }

    protected abstract BeanDefinition getBeanDefinition(String beanName) throws BeansException;

    protected abstract Object createBean(String beanName, BeanDefinition beanDefinition, Object[] args) throws BeansException;

    private boolean isOrdered = false;

    @Override
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        isOrdered = false;
        this.beanPostProcessors.remove(beanPostProcessor);
        this.beanPostProcessors.add(beanPostProcessor);
    }

    public List<BeanPostProcessor> getBeanPostProcessors() {
        // sort BeanPostProcessor
        if (!isOrdered) {
            OrderComparator.sort(this.beanPostProcessors);
            isOrdered = true;
        }
        return this.beanPostProcessors;
    }

    public boolean isCreatingCurrently() {
        return currentlyCreatingBeans.size() != 0;
    }

    public boolean isCreatingCurrently(String beanName) {
        return currentlyCreatingBeans.contains(beanName);
    }

    protected void addCurrentlyCreatingBean(String beanName) {
        currentlyCreatingBeans.add(beanName);
    }

    protected void removeCurrentlyCreatingBean(String beanName) {
        currentlyCreatingBeans.remove(beanName);
    }
}
