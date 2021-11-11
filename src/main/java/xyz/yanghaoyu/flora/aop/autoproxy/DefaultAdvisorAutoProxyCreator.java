package xyz.yanghaoyu.flora.aop.autoproxy;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import xyz.yanghaoyu.flora.aop.*;
import xyz.yanghaoyu.flora.aop.aspectj.AspectJExpressionPointcutAdvisor;
import xyz.yanghaoyu.flora.beans.factory.BeanFactory;
import xyz.yanghaoyu.flora.beans.factory.BeanFactoryAware;
import xyz.yanghaoyu.flora.beans.factory.config.SmartInstantiationAwareBeanPostProcessor;
import xyz.yanghaoyu.flora.beans.factory.support.DefaultListableBeanFactory;
import xyz.yanghaoyu.flora.exception.BeansException;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 按照 advisor 生成代理
 */

public class DefaultAdvisorAutoProxyCreator implements SmartInstantiationAwareBeanPostProcessor, BeanFactoryAware {
    private DefaultListableBeanFactory beanFactory;

    private final Set<Object> earlyProxyReferences = Collections.synchronizedSet(new HashSet<>());

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (DefaultListableBeanFactory) beanFactory;
    }

    private boolean isInfrastructureClass(Class<?> beanClass) {
        return Advice.class.isAssignableFrom(beanClass)
               || Pointcut.class.isAssignableFrom(beanClass)
               || Advisor.class.isAssignableFrom(beanClass);
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
        Collection<AspectJExpressionPointcutAdvisor> advisors = beanFactory.getBeansOfType(AspectJExpressionPointcutAdvisor.class).values();
        Object temp = bean;
        for (AspectJExpressionPointcutAdvisor advisor : advisors) {
            ClassFilter classFilter = advisor.getPointcut().getClassFilter();
            // 过滤匹配类
            if (!classFilter.matches(bean.getClass())) {
                continue;
            }
            AdvisedSupport advisedSupport = new AdvisedSupport();
            TargetSource targetSource = new TargetSource(temp);
            advisedSupport.setTargetSource(targetSource);
            advisedSupport.setMethodInterceptor((MethodInterceptor) advisor.getAdvice());
            advisedSupport.setMethodMatcher(advisor.getPointcut().getMethodMatcher());
            // false -> JDK代理 true -> cglib
            advisedSupport.setProxyTargetClass(false);
            // advisedSupport.setMethodInterceptors(advisor.getAdvices());
            // 返回代理对象
            // 多重代理
            temp = new ProxyFactory(advisedSupport).getProxy();
        }
        return temp;
    }

    @Override
    public Object getEarlyBeanReference(Object bean, String beanName) {
        earlyProxyReferences.add(beanName);
        return wrapIfNecessary(bean, beanName);
    }
}
