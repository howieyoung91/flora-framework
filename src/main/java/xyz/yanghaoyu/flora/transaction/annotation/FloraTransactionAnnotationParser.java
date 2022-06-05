package xyz.yanghaoyu.flora.transaction.annotation;

import xyz.yanghaoyu.flora.transaction.RollbackRuleAttribute;
import xyz.yanghaoyu.flora.transaction.TransactionAttribute;
import xyz.yanghaoyu.flora.transaction.support.RuleBasedTransactionAttribute;

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
        return attribute;
    }
}
