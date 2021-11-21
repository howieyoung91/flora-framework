package xyz.yanghaoyu.flora.util;

import java.util.Collection;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/11/20 16:31<i/>
 * @version 1.0
 */


public abstract class CollectionUtils {
    public static boolean isEmpty(Collection<?> collection) {
        return (collection == null || collection.isEmpty());
    }
}
