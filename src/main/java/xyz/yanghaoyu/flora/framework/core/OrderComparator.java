/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.framework.core;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2022/2/6 16:25<i/>
 * @version 1.0
 */


public class OrderComparator implements Comparator<Object> {
    public static final OrderComparator INSTANCE = new OrderComparator();

    @Override
    public int compare(Object o1, Object o2) {
        if (o1 == null) {
            throw new NullPointerException();
        }
        if (o2 == null) {
            throw new NullPointerException();
        }
        boolean p1 = o1 instanceof PriorityOrdered;
        boolean p2 = o2 instanceof PriorityOrdered;

        if (p1 && !p2) {
            // 优先级更高 返回 -1
            return -1;
        }
        else if (!p1 && p2) {
            return 1;
        }

        int order1 = getOrder(o1);
        int order2 = getOrder(o2);
        return Integer.compare(order1, order2);
    }

    private int getOrder(Object o) {
        return o instanceof Ordered ? ((Ordered) o).getOrder() : Ordered.LOWEST_PRECEDENCE / 2;
    }

    public static void sort(List<?> list) {
        if (list.size() > 1) {
            list.sort(INSTANCE);
        }
    }

    public static void sort(Object[] array) {
        if (array.length > 1) {
            Arrays.sort(array, INSTANCE);
        }
    }

    public static void sortIfNecessary(Object value) {
        if (value instanceof Object[]) {
            sort((Object[]) value);
        }
        else if (value instanceof List) {
            sort((List<?>) value);
        }
    }
}
