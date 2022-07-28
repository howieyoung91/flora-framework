/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.framework.transaction;

import java.lang.reflect.Method;

public interface TransactionAttributeSource {
    TransactionAttribute getTransactionAttribute(Method method, Class<?> targetClass);
}