package xyz.yanghaoyu.flora.core.aop.autoproxy;

import org.aopalliance.aop.Advice;
import xyz.yanghaoyu.flora.core.Ordered;
import xyz.yanghaoyu.flora.core.aop.*;
import xyz.yanghaoyu.flora.core.aop.aspectj.AnnotationAspectJExpressionPointcutAdvisor;
import xyz.yanghaoyu.flora.core.aop.aspectj.AnnotationAspectJExpressionPointcutAdvisorManager;
import xyz.yanghaoyu.flora.core.aop.interceptor.MultiMethodInterceptor;
import xyz.yanghaoyu.flora.core.beans.factory.BeanFactory;
import xyz.yanghaoyu.flora.core.beans.factory.BeanFactoryAware;
import xyz.yanghaoyu.flora.core.beans.factory.config.SmartInstantiationAwareBeanPostProcessor;
import xyz.yanghaoyu.flora.core.beans.factory.support.DefaultListableBeanFactory;
import xyz.yanghaoyu.flora.exception.BeansException;
import xyz.yanghaoyu.flora.util.ReflectUtil;

import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 这个类从 AnnotationAspectJExpressionPointcutAdvisorManager 读入 enhance信息
 * 根据 enhance信息 为 bean 生成代理对象 实现 AOP
 *
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/11/11 21:41<i/>
 * @version 1.0
 * @see AnnotationAspectJExpressionPointcutAdvisorManager
 */

public class AnnotationAwareAspectJAutoProxyCreator
        implements SmartInstantiationAwareBeanPostProcessor, BeanFactoryAware, Ordered {
    protected       DefaultListableBeanFactory beanFactory;
    protected final Set<Object>                earlyProxyReferences
            = Collections.synchronizedSet(new HashSet<>());

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE / 2;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (DefaultListableBeanFactory) beanFactory;
    }

    private boolean isInfrastructureClass(Class<?> beanClass) {
        int modifiers = beanClass.getModifiers();
        return ReflectUtil.isCglibProxyClass(beanClass)
               || Advice.class.isAssignableFrom(beanClass)
               || Pointcut.class.isAssignableFrom(beanClass)
               || Advisor.class.isAssignableFrom(beanClass)
               || Modifier.isFinal(modifiers);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (!earlyProxyReferences.contains(beanName)) {
            return wrapIfNecessary(bean, beanName);
        }
        return bean;
    }

    protected Object wrapIfNecessary(Object bean, String beanName) {
        if (isInfrastructureClass(bean.getClass())) {
            return bean;
        }
        AnnotationAspectJExpressionPointcutAdvisorManager manager = beanFactory.getBean(
                AnnotationAspectJExpressionPointcutAdvisorManager.class.getName(),
                AnnotationAspectJExpressionPointcutAdvisorManager.class
        );

        Collection<AnnotationAspectJExpressionPointcutAdvisor> candidates
                = manager.getAdvisorCandidates(bean.getClass());

        // 没有找到
        if (!shouldProxy(candidates, bean)) {
            return bean;
        }

        // 代理
        TargetSource             targetSource = new TargetSource(bean);
        AnnotationAdvisedSupport support      = new AnnotationAdvisedSupport();
        support.setTargetSource(targetSource);
        // 默认使用 cglib
        support.setProxyTargetClass(true);
        // 把候选的拦截器注入
        for (AnnotationAspectJExpressionPointcutAdvisor advisor : candidates) {
            support.addMethodInterceptor(
                    advisor.getPointcut().getMethodMatcher(),
                    (MultiMethodInterceptor) advisor.getAdvice()
            );
        }
        bean = new AnnotationProxyFactory(support).getProxy();
        return bean;
    }

    private boolean shouldProxy(Collection<AnnotationAspectJExpressionPointcutAdvisor> candidates, Object bean) {
        return candidates.size() != 0;
    }

    @Override
    public Object getEarlyBeanReference(Object bean, String beanName) {
        earlyProxyReferences.add(beanName);
        return wrapIfNecessary(bean, beanName);
    }


}
