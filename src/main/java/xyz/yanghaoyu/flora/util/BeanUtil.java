package xyz.yanghaoyu.flora.util;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2022/2/11 17:25<i/>
 * @version 1.0
 */


public abstract class BeanUtil {
    public static String builtInBeanName(Class<?> builtInBeanClazz) {
        return "flora$" + builtInBeanClazz.getSimpleName() + "$";
    }
}
