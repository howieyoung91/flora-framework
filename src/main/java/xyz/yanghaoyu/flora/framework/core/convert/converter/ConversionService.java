/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.framework.core.convert.converter;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/11/21 17:01<i/>
 * @version 1.0
 */


public interface ConversionService {
    /**
     * Return {@code true} if objects of {@code sourceType} can be converted to the
     * {@code targetType}.
     */
    boolean canConvert(Class<?> sourceType, Class<?> targetType);

    /**
     * Convert the given {@code source} to the specified {@code targetType}.
     */
    <T> T convert(Object source, Class<T> targetType);

    // Object convert(Object source, Class<?> sourceType, Class<?> targetType);
}
