package xyz.yanghaoyu.flora.core.convert.support;

import xyz.yanghaoyu.flora.core.convert.converter.GenericConverter;
import xyz.yanghaoyu.flora.util.CollectionUtil;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/11/24 17:02<i/>
 * @version 1.0
 */

class MapToMapConverter implements GenericConverter {
    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return Collections.singleton(new ConvertiblePair(Map.class, Map.class));
    }

    @Override
    public Object convert(Object source, Class sourceType, Class targetType) {
        if (source == null) {
            return null;
        }

        Map sourceMap = (Map) source;

        int size = sourceMap.size();
        if (size == 0) {
            return sourceMap;
        }

        Map<Object, Object> target = CollectionUtil.createMap(targetType, size);
        target.putAll(sourceMap);

        return target;
    }
}
