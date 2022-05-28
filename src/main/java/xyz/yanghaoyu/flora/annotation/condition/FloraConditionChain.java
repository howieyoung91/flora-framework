/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.annotation.condition;

import xyz.yanghaoyu.flora.annotation.Condition;
import xyz.yanghaoyu.flora.annotation.ConditionContext;
import xyz.yanghaoyu.flora.core.beans.factory.config.BeanDefinition;

import java.util.LinkedList;

public class FloraConditionChain extends FloraCondition {
    private LinkedList<Condition> conditions = new LinkedList<>();

    public FloraConditionChain() {
        conditions.add(new OnBeanCondition());
        conditions.add(new OnMissingBeanCondition());
    }

    @Override
    public boolean matches(ConditionContext context, BeanDefinition beanDef) {
        for (Condition condition : conditions) {
            if (!condition.matches(context, beanDef)) {
                return false;
            }
        }
        return true;
    }
}
