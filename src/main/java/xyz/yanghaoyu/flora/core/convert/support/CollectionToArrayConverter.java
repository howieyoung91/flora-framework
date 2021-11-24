package xyz.yanghaoyu.flora.core.convert.support;

import xyz.yanghaoyu.flora.core.convert.converter.ConversionService;
import xyz.yanghaoyu.flora.core.convert.converter.GenericConverter;

import java.lang.reflect.Array;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/11/23 14:55<i/>
 * @version 1.0
 */

public class CollectionToArrayConverter implements GenericConverter {
    private ConversionService conversionService;

    public CollectionToArrayConverter(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return Collections.singleton(new ConvertiblePair(AbstractCollection.class, Object[].class));
    }

    @Override
    public Object convert(Object source, Class sourceType, Class targetType) {
        Collection srcCollection = (Collection) source;
        Class componentType = targetType.getComponentType();
        int size = srcCollection.size();

        Object target = Array.newInstance(componentType, size);
        int i = 0;
        for (Object field : srcCollection) {
            Object targetElem = conversionService.convert(field, componentType);
            Array.set(target, i, targetElem);
            i++;
        }
        return target;
    }
}
