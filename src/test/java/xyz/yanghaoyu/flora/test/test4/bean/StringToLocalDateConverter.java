package xyz.yanghaoyu.flora.test.test4.bean;

import xyz.yanghaoyu.flora.core.convert.converter.Converter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/11/21 20:18<i/>
 * @version 1.0
 */

public class StringToLocalDateConverter implements Converter<String, LocalDate> {
    private final DateTimeFormatter DATE_TIME_FORMATTER;

    public StringToLocalDateConverter(String pattern) {
        DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(pattern);
    }

    @Override
    public LocalDate convert(String source) {
        return LocalDate.parse(source, DATE_TIME_FORMATTER);
    }
}
