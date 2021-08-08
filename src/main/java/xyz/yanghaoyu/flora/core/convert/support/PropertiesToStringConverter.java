package xyz.yanghaoyu.flora.core.convert.support;

import xyz.yanghaoyu.flora.core.convert.converter.Converter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/11/22 15:49<i/>
 * @version 1.0
 */

class PropertiesToStringConverter implements Converter<Properties, String> {
    @Override
    public String convert(Properties source) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream(256);
            source.store(bos, null);
            return bos.toString("ISO-8859-1");
        } catch (IOException ex) {
            throw new IllegalArgumentException("Failed to store [" + source + "] into String", ex);
        }
    }
}
