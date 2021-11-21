package xyz.yanghaoyu.flora.core.convert.converter;


import static java.util.Objects.requireNonNull;

/**
 * 类型转换器
 *
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/11/16 14:33<i/>
 * @version 1.0
 */
@FunctionalInterface
public interface Converter<S, T> {

    // assert source != null
    T convert(S source);

    default <T1> Converter<S, T1> andThen(Converter<? super T, ? extends T1> after) {
        requireNonNull(after, "After Converter must not be null!");
        return (S s) -> {
            T initialResult = convert(s);
            return (initialResult != null ? after.convert(initialResult) : null);
        };
    }
}
