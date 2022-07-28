/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.framework.core.convert.support;

import xyz.yanghaoyu.flora.framework.core.convert.converter.Converter;
import xyz.yanghaoyu.flora.framework.core.convert.converter.ConverterFactory;
import xyz.yanghaoyu.flora.framework.util.NumberUtil;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/11/18 17:13<i/>
 * @version 1.0
 */


class CharacterToNumberFactory implements ConverterFactory<Character, Number> {
    @Override
    public <T extends Number> Converter<Character, T> getConverter(Class<T> targetType) {
        return source -> NumberUtil.convertNumberToTargetClass((short) source.charValue(), targetType);
    }
}
