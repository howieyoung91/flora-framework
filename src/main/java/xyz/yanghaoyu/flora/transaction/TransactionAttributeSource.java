package xyz.yanghaoyu.flora.transaction;

import java.lang.reflect.Method;

public interface TransactionAttributeSource {
    TransactionAttribute getTransactionAttribute(Method method, Class<?> targetClass);
}