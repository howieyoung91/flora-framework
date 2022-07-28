/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.framework.core.convert.support;

import xyz.yanghaoyu.flora.framework.core.convert.converter.Converter;
import xyz.yanghaoyu.flora.framework.core.convert.converter.ConverterFactory;
import xyz.yanghaoyu.flora.framework.util.NumberUtil;


class StringToNumberConverterFactory implements ConverterFactory<String, Number> {
    @Override
    public <T extends Number> Converter<String, T> getConverter(Class<T> targetType) {
        return source -> source.isEmpty() ? null : NumberUtil.parseNumber(source, targetType);
    }
}
