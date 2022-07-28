/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.framework.transaction.config;

import xyz.yanghaoyu.flora.framework.core.aop.aspectj.AnnotationAspectJExpressionPointcutAdvisorManager;
import xyz.yanghaoyu.flora.framework.core.beans.factory.ConfigurableListableBeanFactory;
import xyz.yanghaoyu.flora.framework.core.beans.factory.config.BeanFactoryPostProcessor;
import xyz.yanghaoyu.flora.framework.exception.BeansException;
import xyz.yanghaoyu.flora.framework.transaction.annotation.Transactional;
import xyz.yanghaoyu.flora.framework.transaction.interceptor.TransactionInterceptor;
import xyz.yanghaoyu.flora.framework.transaction.support.AnnotationTransactionAttributeSource;

public class TransactionPostProcessor implements BeanFactoryPostProcessor {
    public static final String POINTCUT = "@annotation(" + Transactional.class.getName() + ")";

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        AnnotationAspectJExpressionPointcutAdvisorManager advisorManager = getAdvisorManager(beanFactory);
        if (advisorManager == null) {
            return;
        }
        registerInterceptor(beanFactory, advisorManager);
    }

    private AnnotationAspectJExpressionPointcutAdvisorManager getAdvisorManager(ConfigurableListableBeanFactory beanFactory) {
        if (!beanFactory.containsSingletonBean(AnnotationAspectJExpressionPointcutAdvisorManager.class.getName())) {
            return null;
        }
        return beanFactory.getBean(AnnotationAspectJExpressionPointcutAdvisorManager.class.getName(), AnnotationAspectJExpressionPointcutAdvisorManager.class);
    }

    private void registerInterceptor(ConfigurableListableBeanFactory beanFactory, AnnotationAspectJExpressionPointcutAdvisorManager advisorManager) {
        TransactionInterceptor interceptor = new TransactionInterceptor();
        interceptor.setBeanFactory(beanFactory);
        interceptor.setTransactionAttributeSource(new AnnotationTransactionAttributeSource());
        advisorManager.registerMethodEnhanceAdvice(POINTCUT, interceptor);
    }
}
