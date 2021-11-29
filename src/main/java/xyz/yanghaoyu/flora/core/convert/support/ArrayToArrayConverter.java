package xyz.yanghaoyu.flora.core.convert.support;

import xyz.yanghaoyu.flora.core.convert.converter.ConversionService;
import xyz.yanghaoyu.flora.core.convert.converter.GenericConverter;

import java.lang.reflect.Array;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/11/23 20:33<i/>
 * @version 1.0
 */


class ArrayToArrayConverter implements GenericConverter {
    private ConversionService conversionService;

    public ArrayToArrayConverter(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return Collections.singleton(new ConvertiblePair(Object[].class, Object[].class));
    }

    @Override
    public Object convert(Object source, Class sourceType, Class targetType) {

        Class sourceComponentType = sourceType.getComponentType();
        Class targetComponentType = targetType.getComponentType();

        if (Objects.equals(sourceComponentType, targetComponentType)) {
            return source;
        } else {
            Object[] sourceArray = (Object[]) source;
            int length = sourceArray.length;

            Object target = Array.newInstance(targetComponentType, length);
            for (int i = 0; i < length; i++) {
                Array.set(target, i, conversionService.convert(sourceArray[i], targetComponentType));
            }

            return target;
        }
    }
}
