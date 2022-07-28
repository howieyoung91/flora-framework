/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.framework.core;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2022/2/6 16:22<i/>
 * @version 1.0
 */


public interface Ordered {
    int HIGHEST_PRECEDENCE = Integer.MIN_VALUE;
    int LOWEST_PRECEDENCE  = Integer.MAX_VALUE;

    int getOrder();
}
