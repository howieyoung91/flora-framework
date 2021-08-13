package xyz.yanghaoyu.flora.aop;

import java.lang.reflect.Method;

/**
 * @author <a href="https://www.yanghaoyu.xyz">Howie Young</a><i>on 2021/8/12 17:28<i/>
 * @version 1.0
 */


public class AspectJExpressionPointcut implements Pointcut, ClassFilter, MethodMatcher {
    private static final Set<Pointcutcur>
    @Override
    public boolean match(Class<?> clazz) {
        return false;
    }

    @Override
    public boolean match(Method method, Class<?> targetClass) {
        return false;
    }

    @Override
    public ClassFilter getClassFilter() {
        return null;
    }

    @Override
    public MethodMatcher getMethodMatcher() {
        return null;
    }
}
