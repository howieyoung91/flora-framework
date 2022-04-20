package xyz.yanghaoyu.flora.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author <a href="https://www.yanghaoyu.xyz">Howie Young</a><i>on 2021/8/8 22:20<i/>
 * @version 1.0
 */
@Target({})
public @interface Inject {
    @Target({ElementType.FIELD, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    @interface ByType {
        Class<?> DEFAULT_CLASS = Object.class;

        // 默认注入与 field 匹配的 bean, 使用 value可以指定注入类型
        Class<?> value() default Object.class;

        Class<?> clazz() default Object.class;

        boolean required() default true;
    }

    @Target({ElementType.FIELD, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    @interface ByName {
        String value() default Component.DEFAULT_BEAN_NAME;

        String id() default Component.DEFAULT_BEAN_NAME;

        boolean required() default true;
    }
}

