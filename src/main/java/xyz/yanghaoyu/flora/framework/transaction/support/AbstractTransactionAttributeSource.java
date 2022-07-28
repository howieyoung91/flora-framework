/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.framework.transaction.support;

import xyz.yanghaoyu.flora.framework.core.MethodClassKey;
import xyz.yanghaoyu.flora.framework.transaction.TransactionAttribute;
import xyz.yanghaoyu.flora.framework.transaction.TransactionAttributeSource;
import xyz.yanghaoyu.flora.framework.util.ReflectUtil;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractTransactionAttributeSource implements TransactionAttributeSource {
    private static final TransactionAttribute NULL_TRANSACTION_ATTRIBUTE = new DefaultTransactionAttribute() {
        @Override
        public String toString() {return "null";}
    };

    private final Map<Object, TransactionAttribute> cache = new ConcurrentHashMap<>(1024);

    @Override
    public TransactionAttribute getTransactionAttribute(Method method, Class<?> targetClass) {
        if (ReflectUtil.belongToObjectClass(method)) {
            return null;
        }

        MethodClassKey       cacheKey  = MethodClassKey.of(method, targetClass);
        TransactionAttribute attribute = cache.get(cacheKey);
        // should compute?
        if (attribute != null) {
            if (attribute == NULL_TRANSACTION_ATTRIBUTE) {
                return null;
            }
            return attribute;
        }

        attribute = computeTransactionAttribute(method, targetClass);
        cacheAttribute(cacheKey, attribute);
        return attribute;
    }

    private void cacheAttribute(MethodClassKey cacheKey, TransactionAttribute attribute) {
        if (attribute == null) {
            cache.put(cacheKey, NULL_TRANSACTION_ATTRIBUTE);
        }
        else {
            cache.put(cacheKey, attribute);
        }
    }

    private TransactionAttribute computeTransactionAttribute(Method method, Class<?> targetClass) {
        TransactionAttribute attribute = getTransactionAttributeFromMethod(method);
        if (attribute != null) {
            return attribute;
        }
        return getTransactionAttributeFromClass(targetClass);
    }

    private TransactionAttribute getTransactionAttributeFromMethod(Method method) {
        TransactionAttribute attribute = null;
        if (ReflectUtil.isPublic(method)) {
            attribute = findTransactionAttribute(method);
        }
        return attribute;
    }

    private TransactionAttribute getTransactionAttributeFromClass(Class<?> targetClass) {
        return findTransactionAttribute(targetClass);
    }

    protected abstract TransactionAttribute findTransactionAttribute(Method method);

    protected abstract TransactionAttribute findTransactionAttribute(Class<?> clazz);
}
