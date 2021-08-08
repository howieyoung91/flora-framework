package xyz.yanghaoyu.flora.transaction.annotation;

import xyz.yanghaoyu.flora.transaction.TransactionAttribute;

import java.lang.reflect.AnnotatedElement;

public interface TransactionAnnotationParser {
    TransactionAttribute parseTransactionAnnotation(AnnotatedElement element);
}
