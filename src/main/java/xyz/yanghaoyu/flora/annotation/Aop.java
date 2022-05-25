package xyz.yanghaoyu.flora.annotation;

import java.lang.annotation.*;

/**
 * @author <a href="https://www.yanghaoyu.xyz">Howie Young</a><i>on 2021/8/15 17:09<i/>
 * @version 1.0
 */
@Target({})
public @interface Aop {
    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface Enhance {
        String value();
    }
}

