/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.framework.core.convert.support;

import xyz.yanghaoyu.flora.framework.core.convert.converter.GenericConverter;
import xyz.yanghaoyu.flora.framework.util.CollectionUtil;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/11/23 20:44<i/>
 * @version 1.0
 */


class CollectionToCollectionConverter implements GenericConverter {
    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return Collections.singleton(new ConvertiblePair(Collection.class, Collection.class));
    }

    @Override
    public Object convert(Object source, Class sourceType, Class targetType) {

        Collection sourceCollection = (Collection) source;

        int size = sourceCollection.size();
        Collection targetCollection = CollectionUtil.newCollection(targetType, size);

        targetCollection.addAll(sourceCollection);

        return targetCollection;
    }
}
