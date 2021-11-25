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
    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @interface ByType {
        boolean required() default true;
    }

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @interface ByName {
        String value() default "";

        String id() default "";

        boolean required() default true;
    }
}
