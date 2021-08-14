package xyz.yanghaoyu.flora.aop;

/**
 * 类匹配器
 */
public interface ClassFilter {
    boolean matches(Class<?> clazz);
}
