package xyz.yanghaoyu.flora.aop.aspectj;

import xyz.yanghaoyu.flora.aop.ClassFilter;
import xyz.yanghaoyu.flora.aop.MethodMatcher;
import xyz.yanghaoyu.flora.aop.Pointcut;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/11/12 20:01<i/>
 * @version 1.0
 */


public interface AbstractPointcut extends Pointcut, ClassFilter, MethodMatcher {

    default ClassFilter getClassFilter() {
        return this;
    }

    default MethodMatcher getMethodMatcher() {
        return this;
    }
}
