/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.framework.core.convert.support;

import xyz.yanghaoyu.flora.framework.core.convert.converter.Converter;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/11/18 17:19<i/>
 * @version 1.0
 */

class NumberToCharacterConverter implements Converter<Number, Character> {
    @Override
    public Character convert(Number source) {
        return (char) source.shortValue();
    }
}
