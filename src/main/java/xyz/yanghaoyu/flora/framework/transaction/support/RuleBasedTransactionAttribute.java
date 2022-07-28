/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.framework.transaction.support;

import xyz.yanghaoyu.flora.framework.transaction.RollbackRuleAttribute;

import java.util.List;

public class RuleBasedTransactionAttribute extends DefaultTransactionAttribute {
    private List<RollbackRuleAttribute> rollbackRules;

    public void setRollbackRules(List<RollbackRuleAttribute> rollbackRules) {
        this.rollbackRules = rollbackRules;
    }

    @Override
    public boolean rollbackOn(Throwable ex) {
        for (RollbackRuleAttribute rollbackRule : rollbackRules) {
            Class<?> exceptionClass = rollbackRule.getExceptionClass();
            if (exceptionClass.isAssignableFrom(ex.getClass())) {
                return true;
            }
        }
        return false;
    }
}
