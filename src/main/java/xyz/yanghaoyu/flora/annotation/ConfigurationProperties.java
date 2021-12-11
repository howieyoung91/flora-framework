package xyz.yanghaoyu.flora.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/12/11 14:32<i/>
 * @version 1.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigurationProperties {
    String prefix();
}
