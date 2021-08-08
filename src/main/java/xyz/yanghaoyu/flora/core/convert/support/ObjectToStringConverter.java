package xyz.yanghaoyu.flora.core.convert.support;

import xyz.yanghaoyu.flora.core.convert.converter.Converter;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/11/18 16:42<i/>
 * @version 1.0
 */

 class ObjectToStringConverter implements Converter<Object, String> {
    @Override
    public String convert(Object source) {
        return source.toString();
    }
}
