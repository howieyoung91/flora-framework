/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 条件注入
 * <p>
 * flora 的 @Conditional 与 Spring 的 @Conditional 并不相同，
 * Spring 的 @Conditional 是与当前 beanFactory 中的 bean 有关，
 * 而且注入顺序也对该注解有影响。
 * flora 的 @Conditional 总是贪婪的，会尽可能地保证 bean 被注入，
 * 即使一个bean被 @Conditional 标记了，也会先注入容器，然后使用
 * Condition 来判断是否会把该 bean 移出容器
 *
 * @see xyz.yanghaoyu.flora.core.beans.factory.config.GreedyConfigurationClassBeanDefinitionReader
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Conditional {
    Class<? extends Condition>[] value();

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @interface OnBean {
        String[] value() default {};
    }

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @interface OnMissingBean {
        String[] value() default {};
    }

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @interface OnProperty {
        String[] value() default {};
    }

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @interface OnClass {
        String[] value() default {};
    }

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @interface OnMissingClass {
        String[] value() default {};
    }
}
