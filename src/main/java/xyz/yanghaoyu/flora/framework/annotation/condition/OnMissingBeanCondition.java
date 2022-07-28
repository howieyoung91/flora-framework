/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.framework.annotation.condition;

import xyz.yanghaoyu.flora.framework.annotation.ConditionContext;
import xyz.yanghaoyu.flora.framework.annotation.Conditional;
import xyz.yanghaoyu.flora.framework.core.beans.factory.ConfigurableListableBeanFactory;
import xyz.yanghaoyu.flora.framework.core.beans.factory.config.BeanDefinition;

import java.lang.reflect.Method;
import java.util.Arrays;

public class OnMissingBeanCondition extends FloraFrameworkCondition {
    @Override
    public boolean matches(ConditionContext context, BeanDefinition beanDef) {
        Method factoryMethod = beanDef.getFactoryMethod();

        Conditional.OnMissingBean onMissingBeanAnn = factoryMethod.getAnnotation(Conditional.OnMissingBean.class);
        if (onMissingBeanAnn == null) {
            return true;
        }

        String[] missingBeanNames = onMissingBeanAnn.value();
        return doMatch(context, missingBeanNames);
    }

    private boolean doMatch(ConditionContext context, String[] missingBeanNames) {
        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
        return Arrays.stream(missingBeanNames).noneMatch(beanFactory::containsBean);
    }
}
