/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.framework.util;

import xyz.yanghaoyu.flora.framework.core.beans.factory.PropertyPlaceholderConfigurer;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/12/11 15:18<i/>
 * @version 1.0
 */


public abstract class PropertyUtil {
    public static String createPropertyKey(String key) {
        return PropertyPlaceholderConfigurer.DEFAULT_PLACEHOLDER_PREFIX + key + PropertyPlaceholderConfigurer.DEFAULT_PLACEHOLDER_SUFFIX;
    }

    public static boolean isPropertyKey(String key) {
        return key.startsWith(PropertyPlaceholderConfigurer.DEFAULT_PLACEHOLDER_PREFIX)
               && key.endsWith(PropertyPlaceholderConfigurer.DEFAULT_PLACEHOLDER_SUFFIX);
    }
}
