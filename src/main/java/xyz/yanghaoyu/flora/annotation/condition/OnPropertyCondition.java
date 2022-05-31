/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.annotation.condition;

import xyz.yanghaoyu.flora.annotation.ConditionContext;
import xyz.yanghaoyu.flora.annotation.Conditional;
import xyz.yanghaoyu.flora.core.beans.factory.ConfigurableListableBeanFactory;
import xyz.yanghaoyu.flora.core.beans.factory.config.BeanDefinition;
import xyz.yanghaoyu.flora.util.PropertyUtil;

import java.lang.reflect.Method;

public class OnPropertyCondition extends FloraCondition {
    @Override
    public boolean matches(ConditionContext context, BeanDefinition beanDef) {
        Method                 factoryMethod = beanDef.getFactoryMethod();
        Conditional.OnProperty onPropertyAnn = factoryMethod.getAnnotation(Conditional.OnProperty.class);
        if (onPropertyAnn == null) {
            return true;
        }
        return doMatch(context, onPropertyAnn);
    }

    private boolean doMatch(ConditionContext context, Conditional.OnProperty onPropertyAnn) {
        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
        for (String property : onPropertyAnn.value()) {
            String propertyKey = PropertyUtil.createPropertyKey(property);
            String s           = beanFactory.resolveEmbeddedValue(propertyKey);
            if (s == null) {
                return false;
            }
        }
        return true;
    }
}
