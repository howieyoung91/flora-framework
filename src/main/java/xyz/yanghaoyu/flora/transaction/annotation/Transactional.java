package xyz.yanghaoyu.flora.transaction.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Transactional {
    String transactionManager();
    
    // int timeout() default -1;
    //
    // boolean readOnly() default false;

    Class<? extends Throwable>[] rollbackFor() default {RuntimeException.class};
}
