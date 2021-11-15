package xyz.yanghaoyu.flora.core.aop;

/**
 * 类匹配器
 */
public interface ClassFilter {
    boolean matches(Class<?> clazz);
}
