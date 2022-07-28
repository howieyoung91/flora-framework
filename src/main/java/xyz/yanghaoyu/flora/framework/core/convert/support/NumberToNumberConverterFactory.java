/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.framework.core.convert.support;

import xyz.yanghaoyu.flora.framework.core.convert.converter.Converter;
import xyz.yanghaoyu.flora.framework.core.convert.converter.ConverterFactory;
import xyz.yanghaoyu.flora.framework.util.NumberUtil;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/11/18 16:43<i/>
 * @version 1.0
 */

 class NumberToNumberConverterFactory implements ConverterFactory<Number, Number> {
    @Override
    public <T extends Number> Converter<Number, T> getConverter(Class<T> targetType) {
        return source -> NumberUtil.convertNumberToTargetClass(source, targetType);
    }
}
