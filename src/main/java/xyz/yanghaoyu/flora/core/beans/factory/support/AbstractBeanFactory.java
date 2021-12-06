package xyz.yanghaoyu.flora.core.beans.factory.support;

import cn.hutool.core.collection.ConcurrentHashSet;
import xyz.yanghaoyu.flora.core.beans.factory.FactoryBean;
import xyz.yanghaoyu.flora.core.beans.factory.StringValueResolver;
import xyz.yanghaoyu.flora.core.beans.factory.config.BeanDefinition;
import xyz.yanghaoyu.flora.core.beans.factory.config.BeanPostProcessor;
import xyz.yanghaoyu.flora.core.beans.factory.config.ConfigurableBeanFactory;
import xyz.yanghaoyu.flora.core.convert.converter.ConversionService;
import xyz.yanghaoyu.flora.exception.BeansException;
import xyz.yanghaoyu.flora.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 基本实现 BeanFactory 的流程
 * 具体逻辑由子类实现
 */
public abstract class AbstractBeanFactory extends FactoryBeanRegistrySupport implements ConfigurableBeanFactory {

    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

    private final List<StringValueResolver> embeddedValueResolvers = new ArrayList<>();

    private Set<String> currentlyCreatingBeans = new ConcurrentHashSet<>();

    private ConversionService conversionService;

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


    protected abstract boolean containsBeanDefinition(String name);

    private Object getObjectForBeanInstance(Object beanInstance, String beanName) {
        // 如果不是 FactoryBean,直接返回原对象即可
        // &beanName 表示要获取 FactoryBean 对象 那就不调用 FactoryBean#getObject 了
        if (!(beanInstance instanceof FactoryBean) || beanName.charAt(0) == '&') {
            return beanInstance;
        }

        // 先看看缓存中是否存在
        Object object = getCachedObjectForFactoryBean(beanName);

        // 不存在的话 则需要调用 FactoryBean#getObject 进行创建
        if (object == null) {
            FactoryBean<?> factoryBean = (FactoryBean<?>) beanInstance;
            object = getObjectFromFactoryBean(factoryBean, beanName);
        }

        return object;
    }

    @Override
    public void addEmbeddedValueResolver(StringValueResolver valueResolver) {
        this.embeddedValueResolvers.add(valueResolver);
    }

    @Override
    public String resolveEmbeddedValue(String value) {
        String result = value;
        for (StringValueResolver resolver : this.embeddedValueResolvers) {
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
    public <T> T getBean(String name, Class<T> requiredType) {
        return ((T) getBean(name));
    }

    protected abstract BeanDefinition getBeanDefinition(String beanName) throws BeansException;

    protected abstract Object createBean(String beanName, BeanDefinition beanDefinition, Object[] args) throws BeansException;

    @Override
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        this.beanPostProcessors.remove(beanPostProcessor);
        this.beanPostProcessors.add(beanPostProcessor);
    }

    public List<BeanPostProcessor> getBeanPostProcessors() {
        return this.beanPostProcessors;
    }

    public boolean isCurrentlyCreating(String beanName) {
        return this.currentlyCreatingBeans.contains(beanName);
    }

    protected void addCurrentlyCreatingBean(String beanName) {
        this.currentlyCreatingBeans.add(beanName);
    }

    protected void removeCurrentlyCreatingBean(String beanName) {
        this.currentlyCreatingBeans.remove(beanName);
    }
}
