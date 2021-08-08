package xyz.yanghaoyu.flora.core.convert.converter;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/11/16 22:07<i/>
 * @version 1.0
 */

@FunctionalInterface
public interface ConverterFactory<S, R> {
    <T extends R> Converter<S, T> getConverter(Class<T> targetType);
}
