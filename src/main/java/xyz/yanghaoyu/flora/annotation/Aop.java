package xyz.yanghaoyu.flora.annotation;

import java.lang.annotation.*;

/**
 * @author <a href="https://www.yanghaoyu.xyz">Howie Young</a><i>on 2021/8/15 17:09<i/>
 * @version 1.0
 */
// TODO support for @Around
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Aop {

    // 不实现 @Before @After @AfterThrowing @AfterReturning 了
    // @Around 已经够强大了
    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface Enhance {
        String pointcut();

        int priority();
    }

    @interface Enhancer {
        String value() default "";
    }
    // @interface Before {
    //     String value() default "";
    // }
    // @interface After {
    //     String value() default "";
    // }
    //
    // @interface AfterThrowing {
    //     String value() default "";
    // }
    //
    // @interface AfterReturning {
    //     String value() default "";
    // }
}

