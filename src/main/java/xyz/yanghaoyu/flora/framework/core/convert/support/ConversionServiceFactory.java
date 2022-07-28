/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.framework.core.convert.support;

import xyz.yanghaoyu.flora.framework.core.convert.converter.Converter;
import xyz.yanghaoyu.flora.framework.core.convert.converter.ConverterFactory;
import xyz.yanghaoyu.flora.framework.core.convert.converter.ConverterRegistry;
import xyz.yanghaoyu.flora.framework.core.convert.converter.GenericConverter;

import java.util.Set;


public final class ConversionServiceFactory {
    private ConversionServiceFactory() {}

    public static void registerConverters(Set<?> converters, ConverterRegistry registry) {
        for (Object converter : converters) {
            if (converter instanceof GenericConverter) {
                registry.addConverter((GenericConverter) converter);
            } else if (converter instanceof Converter<?, ?>) {
                registry.addConverter((Converter<?, ?>) converter);
            } else if (converter instanceof ConverterFactory<?, ?>) {
                registry.addConverterFactory((ConverterFactory<?, ?>) converter);
            } else {
                throw new IllegalArgumentException("Each converter object must implement one of the " +
                                                   "Converter, ConverterFactory, or GenericConverter interfaces");
            }
        }
    }
}
