/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.framework.transaction.annotation;

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

    Propagation propagation() default Propagation.REQUIRED;

    Class<? extends Throwable>[] rollbackFor() default {RuntimeException.class};
}
