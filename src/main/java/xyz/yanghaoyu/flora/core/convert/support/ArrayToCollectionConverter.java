package xyz.yanghaoyu.flora.core.convert.support;

import xyz.yanghaoyu.flora.core.convert.converter.GenericConverter;
import xyz.yanghaoyu.flora.util.CollectionUtil;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/11/23 14:37<i/>
 * @version 1.0
 */

class ArrayToCollectionConverter implements GenericConverter {
    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return Collections.singleton(new ConvertiblePair(Object[].class, Collection.class));
    }

    @Override
    public Object convert(Object source, Class sourceType, Class targetType) {
        Object[] sources = (Object[]) source;
        int length = sources.length;

        Collection targetCollection = CollectionUtil.newCollection(targetType, length);
        Collections.addAll(targetCollection, sources);

        return targetCollection;
    }
}
