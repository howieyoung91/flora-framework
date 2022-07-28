/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.framework.transaction.annotation;

import xyz.yanghaoyu.flora.framework.transaction.RollbackRuleAttribute;
import xyz.yanghaoyu.flora.framework.transaction.TransactionAttribute;
import xyz.yanghaoyu.flora.framework.transaction.support.RuleBasedTransactionAttribute;

import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FloraTransactionAnnotationParser implements TransactionAnnotationParser {
    @Override
    public TransactionAttribute parseTransactionAnnotation(AnnotatedElement element) {
        Transactional transactionalAnn = element.getAnnotation(Transactional.class);
        if (transactionalAnn == null) {
            return null;
        }
        return doParseTransactionAnnotation(transactionalAnn);
    }

    private RuleBasedTransactionAttribute doParseTransactionAnnotation(Transactional transactionalAnn) {
        Class<? extends Throwable>[] rollbackFor = transactionalAnn.rollbackFor();

        RuleBasedTransactionAttribute attribute = new RuleBasedTransactionAttribute();
        List<RollbackRuleAttribute> rollbackRules = Arrays.stream(rollbackFor)
                .map(RollbackRuleAttribute::new)
                .collect(Collectors.toCollection(ArrayList::new));

        attribute.setTransactionManager(transactionalAnn.transactionManager());
        attribute.setRollbackRules(rollbackRules);
        attribute.setPropagationBehavior(transactionalAnn.propagation().value());
        return attribute;
    }
}
