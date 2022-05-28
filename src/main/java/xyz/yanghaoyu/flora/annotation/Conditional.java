/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({})
@Retention(RetentionPolicy.RUNTIME)
public @interface Conditional {
    // Class<? extends Condition>[] value();

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @interface OnBean {
        String[] value() default {};
    }

    // @Target({ElementType.METHOD})
    // todo 暂不启用
    @Target({})
    @Retention(RetentionPolicy.RUNTIME)
    @interface OnMissingBean {
        String[] value() default {};
    }
}
