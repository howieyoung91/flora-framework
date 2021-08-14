package xyz.yanghaoyu.flora.aop;

import java.lang.reflect.Method;

/**
 * 方法匹配器
 * <p>
 * 在目标类下找到对应的方法
 */

public interface MethodMatcher {
    boolean matches(Method method, Class<?> targetClass);
}
