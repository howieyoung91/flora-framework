package xyz.yanghaoyu.flora.core.convert.converter.support;

import xyz.yanghaoyu.flora.core.convert.converter.Converter;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/11/18 17:00<i/>
 * @version 1.0
 */


public class EnumToStringConverter implements Converter<Enum, String> {
    @Override
    public String convert(Enum source) {
        return source.toString();
    }
}
