package xyz.yanghaoyu.flora.aop.autoproxy.annotation;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import xyz.yanghaoyu.flora.aop.*;
import xyz.yanghaoyu.flora.aop.aspectj.AnnotationAspectJExpressionPointcutAdvisor;
import xyz.yanghaoyu.flora.aop.aspectj.AnnotationAspectJExpressionPointcutAdvisorManager;
import xyz.yanghaoyu.flora.beans.factory.BeanFactory;
import xyz.yanghaoyu.flora.beans.factory.BeanFactoryAware;
import xyz.yanghaoyu.flora.beans.factory.config.SmartInstantiationAwareBeanPostProcessor;
import xyz.yanghaoyu.flora.beans.factory.support.DefaultListableBeanFactory;
import xyz.yanghaoyu.flora.exception.BeansException;

import java.util.*;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/11/11 21:41<i/>
 * @version 1.0
 */

public class AnnotationAwareAspectJAutoProxyCreator implements SmartInstantiationAwareBeanPostProcessor, BeanFactoryAware {
    protected DefaultListableBeanFactory beanFactory;

    protected final Set<Object> earlyProxyReferences = Collections.synchronizedSet(new HashSet<>());

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
        AnnotationAspectJExpressionPointcutAdvisorManager manager = beanFactory.getBean(AnnotationAspectJExpressionPointcutAdvisorManager.class.getName(), AnnotationAspectJExpressionPointcutAdvisorManager.class);
        Map<String, AnnotationAspectJExpressionPointcutAdvisor> map = manager.getMap();
        Collection<AnnotationAspectJExpressionPointcutAdvisor> advisors = map.values();
        for (AnnotationAspectJExpressionPointcutAdvisor advisor : advisors) {
            ClassFilter classFilter = advisor.getPointcut().getClassFilter();
            // 过滤匹配类
            if (!classFilter.matches(bean.getClass())) {
                continue;
            }
            AdvisedSupport advisedSupport = new AdvisedSupport();
            TargetSource targetSource = new TargetSource(bean);
            // 把真实对象注入
            advisedSupport.setTargetSource(targetSource);
            // 方法拦截器 怎样增强
            advisedSupport.setMethodInterceptor((MethodInterceptor) advisor.getAdvice());
            // 方法匹配器 增强哪个方法
            advisedSupport.setMethodMatcher(advisor.getPointcut().getMethodMatcher());
            // false -> JDK代理 true -> cglib
            advisedSupport.setProxyTargetClass(true);
            // advisedSupport.setMethodInterceptors(advisor.getAdvices());
            // 返回代理对象
            return new ProxyFactory(advisedSupport).getProxy();
        }
        return bean;
    }

    @Override
    public Object getEarlyBeanReference(Object bean, String beanName) {
        earlyProxyReferences.add(beanName);
        return wrapIfNecessary(bean, beanName);
    }
}
