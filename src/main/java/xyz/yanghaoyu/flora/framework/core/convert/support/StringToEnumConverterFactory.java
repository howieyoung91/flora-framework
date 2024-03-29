/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.framework.core.convert.support;

import xyz.yanghaoyu.flora.framework.core.convert.converter.Converter;
import xyz.yanghaoyu.flora.framework.core.convert.converter.ConverterFactory;
import xyz.yanghaoyu.flora.framework.util.ConversionUtil;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/11/18 17:01<i/>
 * @version 1.0
 */


 class StringToEnumConverterFactory implements ConverterFactory<String, Enum> {
    @Override
    public <T extends Enum> Converter<String, T> getConverter(Class<T> targetType) {
        return source -> source.isEmpty()
                ? null
                : (T) Enum.valueOf(
                (Class<T>) ConversionUtil.getEnumType(targetType),
                source.trim()
        );
    }
}
