package xyz.yanghaoyu.flora.core.convert.support;

import xyz.yanghaoyu.flora.core.convert.converter.Converter;
import xyz.yanghaoyu.flora.core.convert.converter.ConverterFactory;
import xyz.yanghaoyu.flora.util.NumberUtil;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/11/18 17:13<i/>
 * @version 1.0
 */


class CharacterToNumberFactory implements ConverterFactory<Character, Number> {
    @Override
    public <T extends Number> Converter<Character, T> getConverter(Class<T> targetType) {
        return source -> NumberUtil.convertNumberToTargetClass((short) source.charValue(), targetType);
    }
}
