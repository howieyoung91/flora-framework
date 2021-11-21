package xyz.yanghaoyu.flora.core.convert.converter.support;

import xyz.yanghaoyu.flora.core.convert.converter.Converter;
import xyz.yanghaoyu.flora.core.convert.converter.ConverterFactory;
import xyz.yanghaoyu.flora.util.NumberUtil;


public class StringToNumberConverterFactory implements ConverterFactory<String, Number> {
    @Override
    public <T extends Number> Converter<String, T> getConverter(Class<T> targetType) {
        return source -> source.isEmpty() ? null : NumberUtil.parseNumber(source, targetType);
    }
}
