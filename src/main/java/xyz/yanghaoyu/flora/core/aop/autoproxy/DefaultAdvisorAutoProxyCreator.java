package xyz.yanghaoyu.flora.core.aop.autoproxy;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import xyz.yanghaoyu.flora.core.aop.*;
import xyz.yanghaoyu.flora.core.aop.aspectj.AspectJExpressionPointcutAdvisor;
import xyz.yanghaoyu.flora.core.beans.factory.BeanFactory;
import xyz.yanghaoyu.flora.core.beans.factory.BeanFactoryAware;
import xyz.yanghaoyu.flora.core.beans.factory.config.SmartInstantiationAwareBeanPostProcessor;
import xyz.yanghaoyu.flora.core.beans.factory.support.DefaultListableBeanFactory;
import xyz.yanghaoyu.flora.exception.BeansException;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 按照 advisor 生成代理
 */
@Deprecated
public class DefaultAdvisorAutoProxyCreator implements SmartInstantiationAwareBeanPostProcessor, BeanFactoryAware {
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
        Collection<AspectJExpressionPointcutAdvisor> advisors =
                beanFactory.getBeansOfType(AspectJExpressionPointcutAdvisor.class).values();
        Object temp = bean;
        for (AspectJExpressionPointcutAdvisor advisor : advisors) {
            ClassFilter classFilter = advisor.getPointcut().getClassFilter();
            // 过滤匹配类
            if (!classFilter.matches(bean.getClass())) {
                continue;
            }
            AdvisedSupport advisedSupport = new AdvisedSupport();
            TargetSource targetSource = new TargetSource(temp);
            // 把真实对象注入
            advisedSupport.setTargetSource(targetSource);
            // 方法拦截器 怎样增强
            advisedSupport.setMethodInterceptor((MethodInterceptor) advisor.getAdvice());
            // 方法匹配器 增强哪个方法
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
