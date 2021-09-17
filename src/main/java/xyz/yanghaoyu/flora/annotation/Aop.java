package xyz.yanghaoyu.flora.annotation;

import java.lang.annotation.*;

/**
 * @author <a href="https://www.yanghaoyu.xyz">Howie Young</a><i>on 2021/8/15 17:09<i/>
 * @version 1.0
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Aop {
    @interface Before {

    }

    @interface Around {

    }

    @interface After {

    }

    @interface AfterThrowing {

    }
}
