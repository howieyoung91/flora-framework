package xyz.yanghaoyu.flora.core.convert.support;

import xyz.yanghaoyu.flora.core.convert.converter.Converter;

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
