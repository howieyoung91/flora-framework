package xyz.yanghaoyu.flora.util;

import xyz.yanghaoyu.flora.core.beans.factory.PropertyPlaceholderConfigurer;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/12/11 15:18<i/>
 * @version 1.0
 */


public abstract class PropertyUtil {
    public static String createPropertyKey(String key) {
        return PropertyPlaceholderConfigurer.DEFAULT_PLACEHOLDER_PREFIX + key + PropertyPlaceholderConfigurer.DEFAULT_PLACEHOLDER_SUFFIX;
    }
}
