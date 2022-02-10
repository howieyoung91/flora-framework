package xyz.yanghaoyu.flora.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * TODO support
 *
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2022/2/8 18:46<i/>
 * @version 1.0
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Order {
    int value();
}
