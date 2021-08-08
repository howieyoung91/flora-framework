package xyz.yanghaoyu.flora.core.convert.support;

import xyz.yanghaoyu.flora.core.convert.converter.ConversionService;
import xyz.yanghaoyu.flora.core.convert.converter.GenericConverter;
import xyz.yanghaoyu.flora.util.StringUtil;

import java.lang.reflect.Array;
import java.util.Collections;
import java.util.Set;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/11/22 20:15<i/>
 * @version 1.0
 */


class StringToArrayConverter implements GenericConverter {
    private ConversionService conversionService;

    public StringToArrayConverter(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return Collections.singleton(new ConvertiblePair(String.class, Object[].class));
    }

    @Override
    public Object convert(Object source, Class sourceType, Class targetType) {
        if (source == null) {
            return null;
        }

        String src = (String) source;
        Class componentType = targetType.getComponentType();

        String[] fields = StringUtil.commaDelimitedListToStringArray(src);

        // new 出数组
        Object target = Array.newInstance(targetType.getComponentType(), fields.length);
        for (int i = 0; i < fields.length; i++) {
            String sourceElem = fields[i];
            Object targetElem = conversionService.convert(sourceElem, componentType);
            Array.set(target, i, targetElem);
        }
        return target;
    }
}
