package xyz.yanghaoyu.flora.core.convert.converter.support;

import xyz.yanghaoyu.flora.core.convert.converter.Converter;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/11/18 17:23<i/>
 * @version 1.0
 */


public class StringToCharacterConverter implements Converter<String, Character> {
    @Override
    public Character convert(String source) {
        if (source.isEmpty()) {
            return null;
        }
        if (source.length() > 1) {
            throw new IllegalArgumentException(
                    "Can only convert a [String] with length of 1 to a [Character]; string value '" + source + "'  has length of " + source.length());
        }
        return source.charAt(0);
    }
}
