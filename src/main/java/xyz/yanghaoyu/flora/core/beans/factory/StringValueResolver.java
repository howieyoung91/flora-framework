package xyz.yanghaoyu.flora.core.beans.factory;

/**
 * @author <a href="https://www.yanghaoyu.xyz">Howie Young</a><i>on 2021/8/14 20:52<i/>
 * @version 1.0
 */

@FunctionalInterface
public interface StringValueResolver {
    String resolveStringValue(String strVal);
}
