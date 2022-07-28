/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.framework.util;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2022/2/11 17:25<i/>
 * @version 1.0
 */


public abstract class BeanUtil {
    public static String builtInBeanName(Class<?> builtInBeanClazz) {
        return "flora-framework$" + builtInBeanClazz.getSimpleName() + "$";
    }
}
