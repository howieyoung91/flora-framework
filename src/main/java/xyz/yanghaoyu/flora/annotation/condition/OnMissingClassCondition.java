/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.annotation.condition;

import xyz.yanghaoyu.flora.annotation.ConditionContext;
import xyz.yanghaoyu.flora.annotation.Conditional;
import xyz.yanghaoyu.flora.core.beans.factory.config.BeanDefinition;

public class OnMissingClassCondition extends FloraFrameworkCondition {
    @Override
    public boolean matches(ConditionContext context, BeanDefinition beanDef) {
        Conditional.OnMissingClass onMissingClass = beanDef.getFactoryMethod().getAnnotation(Conditional.OnMissingClass.class);
        if (onMissingClass == null) {
            return true;
        }
        return doMatch(context, onMissingClass);
    }

    private boolean doMatch(ConditionContext context, Conditional.OnMissingClass onMissingClass) {
        ClassLoader loader = context.getClassLoader();
        for (String className : onMissingClass.value()) {
            try {
                loader.loadClass(className);
                return false;
            }
            catch (ClassNotFoundException ignored) {
            }
        }
        return true;
    }
}
