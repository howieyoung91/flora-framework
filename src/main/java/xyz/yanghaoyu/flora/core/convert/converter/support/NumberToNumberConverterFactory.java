package xyz.yanghaoyu.flora.core.convert.converter.support;

import xyz.yanghaoyu.flora.core.convert.converter.Converter;
import xyz.yanghaoyu.flora.core.convert.converter.ConverterFactory;
import xyz.yanghaoyu.flora.util.NumberUtil;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/11/18 16:43<i/>
 * @version 1.0
 */

public class NumberToNumberConverterFactory implements ConverterFactory<Number, Number> {
    @Override
    public <T extends Number> Converter<Number, T> getConverter(Class<T> targetType) {
        return source -> NumberUtil.convertNumberToTargetClass(source, targetType);
    }
}
