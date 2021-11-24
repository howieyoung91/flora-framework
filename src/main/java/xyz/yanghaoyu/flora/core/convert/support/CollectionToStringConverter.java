package xyz.yanghaoyu.flora.core.convert.support;

import xyz.yanghaoyu.flora.core.convert.converter.ConversionService;
import xyz.yanghaoyu.flora.core.convert.converter.GenericConverter;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.StringJoiner;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/11/23 10:28<i/>
 * @version 1.0
 */

class CollectionToStringConverter implements GenericConverter {
    private ConversionService conversionService;

    public CollectionToStringConverter(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        // 先实现 AbstractCollection TODO Collection
        return Collections.singleton(new ConvertiblePair(Collection.class, String.class));
    }

    private static final String DELIMITER = ",";

    @Override
    public Object convert(Object source, Class sourceType, Class targetType) {
        if (source == null) {
            return null;
        }
        Collection sourceCollection = (Collection) source;
        if (sourceCollection.isEmpty()) {
            return "";
        }


        StringJoiner sj = new StringJoiner(DELIMITER);
        for (Object sourceElement : sourceCollection) {
            Object targetElement = this.conversionService.convert(sourceElement, String.class);
            sj.add(String.valueOf(targetElement));
        }

        return sj.toString();
    }
}
