package xyz.yanghaoyu.flora.core.context.support;

import xyz.yanghaoyu.flora.core.beans.factory.FactoryBean;
import xyz.yanghaoyu.flora.core.beans.factory.support.InitializingBean;
import xyz.yanghaoyu.flora.core.convert.converter.*;
import xyz.yanghaoyu.flora.core.convert.support.DefaultConversionService;

import java.util.Set;

/**
 * 类型转换
 *
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/11/21 17:57<i/>
 * @version 1.0
 */

public class ConversionServiceFactoryBean implements FactoryBean<ConversionService>, InitializingBean {
    private Set<?> converters;
    private GenericConversionService conversionService;
    @Override
    public ConversionService getObject() throws Exception {
        return conversionService;
    }

    @Override
    public Class<?> getObjectType() {
        return GenericConversionService.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.conversionService = new DefaultConversionService();
        registerConverters(converters, conversionService);
    }

    private void registerConverters(Set<?> converters, ConverterRegistry registry) {
        // 添加
        if (converters != null) {
            for (Object converter : converters) {
                if (converter instanceof GenericConverter) {
                    registry.addConverter((GenericConverter) converter);
                } else if (converter instanceof Converter<?, ?>) {
                    registry.addConverter((Converter<?, ?>) converter);
                } else if (converter instanceof ConverterFactory<?, ?>) {
                    registry.addConverterFactory((ConverterFactory<?, ?>) converter);
                } else {
                    throw new IllegalArgumentException(
                            "Each converter object must implement one of the"
                            + " Converter, ConverterFactory, or GenericConverter interfaces");
                }
            }
        }
    }

    public void setConverters(Set<?> converters) {
        this.converters = converters;
    }
}
