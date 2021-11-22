package xyz.yanghaoyu.flora.core.beans.factory.config;

import xyz.yanghaoyu.flora.core.beans.factory.HierarchicalBeanFactory;
import xyz.yanghaoyu.flora.core.beans.factory.StringValueResolver;
import xyz.yanghaoyu.flora.core.convert.converter.ConversionService;


public interface ConfigurableBeanFactory extends HierarchicalBeanFactory, SingletonBeanRegistry {
    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);

    void destroySingletons();

    /**
     * Add a String resolver for embedded values such as annotation attributes.
     *
     * @param valueResolver the String resolver to apply to embedded values
     */
    void addEmbeddedValueResolver(StringValueResolver valueResolver);

    /**
     * Resolve the given embedded value, e.g. an annotation attribute.
     *
     * @param value the value to resolve
     * @return the resolved value (may be the original value as-is)
     */
    String resolveEmbeddedValue(String value);

    void setConversionService(ConversionService conversionService);

    ConversionService getConversionService();
}