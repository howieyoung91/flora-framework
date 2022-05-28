/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.annotation.condition;

import xyz.yanghaoyu.flora.annotation.ConditionContext;
import xyz.yanghaoyu.flora.annotation.Conditional;
import xyz.yanghaoyu.flora.core.beans.factory.ConfigurableListableBeanFactory;
import xyz.yanghaoyu.flora.core.beans.factory.config.BeanDefinition;

import java.lang.reflect.Method;
import java.util.Arrays;

public class OnBeanCondition extends FloraCondition {
    @Override
    public boolean matches(ConditionContext context, BeanDefinition beanDef) {
        Method factoryMethod = beanDef.getFactoryMethod();

        Conditional.OnBean onBeanAnn = factoryMethod.getAnnotation(Conditional.OnBean.class);
        if (onBeanAnn == null) {
            return true;
        }
        String[] dependOnBeanNames = onBeanAnn.value();
        return doMatch(context, dependOnBeanNames);
    }

    private boolean doMatch(ConditionContext context, String[] dependOnBeanNames) {
        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
        return Arrays.stream(dependOnBeanNames).allMatch(beanFactory::containsBean);
    }
}
