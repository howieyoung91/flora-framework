package xyz.yanghaoyu.flora.core.beans.factory.config;

import xyz.yanghaoyu.flora.core.beans.factory.HierarchicalBeanFactory;
import xyz.yanghaoyu.flora.core.beans.factory.StringValueResolver;
import xyz.yanghaoyu.flora.core.convert.converter.ConversionService;


public interface ConfigurableBeanFactory extends HierarchicalBeanFactory, SingletonBeanRegistry {
    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);

    void destroySingletons();

    void addEmbeddedValueResolver(StringValueResolver valueResolver);

    String resolveEmbeddedValue(String value);

    void setConversionService(ConversionService conversionService);

    ConversionService getConversionService();
}
