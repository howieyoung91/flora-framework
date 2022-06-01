/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.annotation.condition;

import xyz.yanghaoyu.flora.annotation.ConditionContext;
import xyz.yanghaoyu.flora.annotation.Conditional;
import xyz.yanghaoyu.flora.core.beans.factory.config.BeanDefinition;

public class OnClassCondition extends FloraFrameworkCondition {
    @Override
    public boolean matches(ConditionContext context, BeanDefinition beanDef) {
        Conditional.OnClass onClassAnn = beanDef.getFactoryMethod().getAnnotation(Conditional.OnClass.class);
        if (onClassAnn == null) {
            return true;
        }
        return doMatch(context, onClassAnn);
    }

    private boolean doMatch(ConditionContext context, Conditional.OnClass onClassAnn) {
        ClassLoader loader = context.getClassLoader();
        try {
            for (String className : onClassAnn.value()) {
                loader.loadClass(className);
            }
        } catch (ClassNotFoundException e) {
            return false;
        }
        return true;
    }
}
