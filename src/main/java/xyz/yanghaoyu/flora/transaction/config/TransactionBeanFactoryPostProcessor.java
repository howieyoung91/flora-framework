/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.transaction.config;

import xyz.yanghaoyu.flora.core.aop.aspectj.AnnotationAspectJExpressionPointcutAdvisorManager;
import xyz.yanghaoyu.flora.core.beans.factory.ConfigurableListableBeanFactory;
import xyz.yanghaoyu.flora.core.beans.factory.config.BeanFactoryPostProcessor;
import xyz.yanghaoyu.flora.exception.BeansException;
import xyz.yanghaoyu.flora.transaction.interceptor.TransactionInterceptor;
import xyz.yanghaoyu.flora.transaction.support.AnnotationTransactionAttributeSource;

public class TransactionBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
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
        advisorManager.addMethodEnhanceAdvice("@annotation(xyz.yanghaoyu.flora.transaction.annotation.Transactional)", interceptor);
    }
}
