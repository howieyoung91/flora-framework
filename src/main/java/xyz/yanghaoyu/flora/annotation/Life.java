package xyz.yanghaoyu.flora.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/12/6 18:02<i/>
 * @version 1.0
 */
@Target({})
public @interface Life {
    // Initialize Method 是不支持 aop 的
    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @interface Initialize {
    }

    // Destroy Method 支持 aop
    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @interface Destroy {
    }
}
