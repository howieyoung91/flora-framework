/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.framework.annotation.condition;

import xyz.yanghaoyu.flora.framework.annotation.Condition;
import xyz.yanghaoyu.flora.framework.annotation.ConditionContext;
import xyz.yanghaoyu.flora.framework.annotation.Conditional;
import xyz.yanghaoyu.flora.framework.core.beans.factory.config.BeanDefinition;

import java.util.HashMap;
import java.util.LinkedList;

public class FloraFrameworkConditionChain extends FloraFrameworkCondition {
    private LinkedList<Condition>        builtInConditions    = new LinkedList<>();
    private HashMap<Class<?>, Condition> customizedConditions = new HashMap<>();

    public FloraFrameworkConditionChain() {
        builtInConditions.add(new OnBeanCondition());
        builtInConditions.add(new OnMissingBeanCondition());
        builtInConditions.add(new OnPropertyCondition());
        builtInConditions.add(new OnClassCondition());
        builtInConditions.add(new OnMissingClassCondition());
    }

    @Override
    public boolean matches(ConditionContext context, BeanDefinition beanDef) {
        return filterByCustomizedConditions(context, beanDef)
               && filterByBuiltInConditions(context, beanDef);
    }

    private boolean filterByBuiltInConditions(ConditionContext context, BeanDefinition beanDef) {
        for (Condition condition : builtInConditions) {
            if (!condition.matches(context, beanDef)) {
                return false;
            }
        }
        return true;
    }

    private boolean filterByCustomizedConditions(ConditionContext context, BeanDefinition beanDef) {
        Conditional conditional = beanDef.getFactoryMethod().getAnnotation(Conditional.class);
        if (conditional == null) {
            return true;
        }

        // filter
        try {
            for (Class<? extends Condition> conditionClass : conditional.value()) {
                if (!getCondition(conditionClass).matches(context, beanDef)) {
                    return false;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    private Condition getCondition(Class<? extends Condition> conditionClass) throws InstantiationException, IllegalAccessException {
        Condition condition = customizedConditions.get(conditionClass);
        if (condition == null) {
            condition = conditionClass.newInstance();
            customizedConditions.put(conditionClass, condition);
        }
        return condition;
    }
}
