/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.framework.transaction.annotation;

import xyz.yanghaoyu.flora.framework.transaction.TransactionAttribute;

import java.lang.reflect.AnnotatedElement;

public interface TransactionAnnotationParser {
    TransactionAttribute parseTransactionAnnotation(AnnotatedElement element);
}
