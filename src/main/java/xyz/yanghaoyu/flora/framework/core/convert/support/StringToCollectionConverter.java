/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.framework.core.convert.support;

import xyz.yanghaoyu.flora.framework.core.convert.converter.ConversionService;
import xyz.yanghaoyu.flora.framework.core.convert.converter.GenericConverter;
import xyz.yanghaoyu.flora.framework.util.CollectionUtil;
import xyz.yanghaoyu.flora.framework.util.StringUtil;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/11/23 10:48<i/>
 * @version 1.0
 */


class StringToCollectionConverter implements GenericConverter {
    private ConversionService conversionService;

    public StringToCollectionConverter(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return Collections.singleton(new ConvertiblePair(String.class, Collection.class));
    }


    @Override
    public Object convert(Object source, Class sourceType, Class targetType) {
        if (source == null) {
            return null;
        }

        String sourceString = (String) source;
        String[] fields = StringUtil.commaDelimitedListToStringArray(sourceString);
        int length = fields.length;

        Collection targetCollection = CollectionUtil.newCollection(targetType, length);
        Collections.addAll(targetCollection, fields);

        return targetCollection;
    }
}
