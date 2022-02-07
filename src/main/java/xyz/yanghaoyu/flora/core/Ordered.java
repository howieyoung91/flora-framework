package xyz.yanghaoyu.flora.core;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2022/2/6 16:22<i/>
 * @version 1.0
 */


public interface Ordered {
    int HIGHEST_PRECEDENCE = Integer.MIN_VALUE;
    int LOWEST_PRECEDENCE = Integer.MAX_VALUE;

    int getOrder();
}
