package xyz.yanghaoyu.flora.core.convert.support;

import xyz.yanghaoyu.flora.core.convert.converter.ConversionService;
import xyz.yanghaoyu.flora.core.convert.converter.GenericConverter;
import xyz.yanghaoyu.flora.util.ObjectUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/11/23 10:26<i/>
 * @version 1.0
 */

 class ArrayToStringConverter implements GenericConverter {
    private final CollectionToStringConverter helperConverter;

    public ArrayToStringConverter(ConversionService conversionService) {
        this.helperConverter = new CollectionToStringConverter(conversionService);
    }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return Collections.singleton(new ConvertiblePair(Object[].class, String.class));
    }

    @Override
    public Object convert(Object source, Class sourceType, Class targetType) {
        return this.helperConverter.convert(Arrays.asList(ObjectUtil.toObjectArray(source)), sourceType, targetType);
    }
}
