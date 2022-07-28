/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */
package xyz.yanghaoyu.flora.framework.transaction.support;

import xyz.yanghaoyu.flora.framework.transaction.annotation.FloraTransactionAnnotationParser;
import xyz.yanghaoyu.flora.framework.transaction.annotation.TransactionAnnotationParser;
import xyz.yanghaoyu.flora.framework.transaction.TransactionAttribute;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

public class AnnotationTransactionAttributeSource extends AbstractTransactionAttributeSource {
    private final boolean                          publicMethodsOnly;
    private final Set<TransactionAnnotationParser> parsers
            = Collections.singleton(new FloraTransactionAnnotationParser());

    public AnnotationTransactionAttributeSource() {
        this(true);
    }

    public AnnotationTransactionAttributeSource(boolean publicMethodsOnly) {
        this.publicMethodsOnly = publicMethodsOnly;
    }

    @Override
    protected TransactionAttribute findTransactionAttribute(Method method) {
        return determineTransactionAttribute(method);
    }

    @Override
    public TransactionAttribute findTransactionAttribute(Class<?> clazz) {
        return determineTransactionAttribute(clazz);
    }

    protected TransactionAttribute determineTransactionAttribute(AnnotatedElement element) {
        return parsers.stream().map(parser -> parser.parseTransactionAnnotation(element))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }
}
