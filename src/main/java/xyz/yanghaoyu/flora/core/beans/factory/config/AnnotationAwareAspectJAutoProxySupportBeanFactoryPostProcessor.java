package xyz.yanghaoyu.flora.core.beans.factory.config;

import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.yanghaoyu.flora.annotation.Aop;
import xyz.yanghaoyu.flora.annotation.Order;
import xyz.yanghaoyu.flora.core.Ordered;
import xyz.yanghaoyu.flora.core.PriorityOrdered;
import xyz.yanghaoyu.flora.core.aop.aspectj.AnnotationAspectJExpressionPointcutAdvisorManager;
import xyz.yanghaoyu.flora.core.aop.interceptor.AdviceChain;
import xyz.yanghaoyu.flora.core.aop.interceptor.AdvicePoint;
import xyz.yanghaoyu.flora.core.beans.factory.ConfigurableListableBeanFactory;
import xyz.yanghaoyu.flora.exception.BeansException;

import java.lang.reflect.Method;

/**
 * 注解实现 aop 支持
 * 这个类会获取 @Aspect 的 类 然后优先生成切面 这一步是必要的
 *
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/11/14 22:45<i/>
 * @version 1.0
 */

public class AnnotationAwareAspectJAutoProxySupportBeanFactoryPostProcessor
        implements BeanFactoryPostProcessor, PriorityOrdered {
    private static final Logger LOGGER = LoggerFactory.getLogger(AnnotationAwareAspectJAutoProxySupportBeanFactoryPostProcessor.class);

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        LOGGER.trace("init [Aspect] ...");
        AnnotationAspectJExpressionPointcutAdvisorManager manager = makeSureAdvisorManager(beanFactory);

        String[] beanNames = beanFactory.getBeanDefinitionNames();
        for (String beanName : beanNames) {
            // 首先 生成 Aspect
            Class<?> clazz = beanFactory.getBeanDefinition(beanName).getBeanClass();
            // 是否标记 @Aspect
            Aspect aspectAnn = clazz.getAnnotation(Aspect.class);
            if (aspectAnn != null) {
                Object   bean    = beanFactory.getBean(beanName);
                Method[] methods = clazz.getDeclaredMethods();

                // 查找被 @Enhance 标记的方法
                for (Method method : methods) {
                    Aop.Enhance enhanceAnn = method.getAnnotation(Aop.Enhance.class);
                    if (enhanceAnn == null) {
                        continue;
                    }

                    LOGGER.trace("register [Pointcut] [{}]", enhanceAnn.value());
                    manager.registerMethodEnhanceAdvice(enhanceAnn.value(), new Point(bean, method, determineAdviceOrder(method)));
                }
            }
        }
    }

    private AnnotationAspectJExpressionPointcutAdvisorManager makeSureAdvisorManager(ConfigurableListableBeanFactory beanFactory) {
        AnnotationAspectJExpressionPointcutAdvisorManager manager = null;
        Class<AnnotationAspectJExpressionPointcutAdvisorManager> managerClass
                = AnnotationAspectJExpressionPointcutAdvisorManager.class;
        String managerName = managerClass.getName();
        if (beanFactory.containsSingletonBean(managerName)) {
            manager = beanFactory.getBean(managerName, managerClass);
        }
        else {
            manager = new AnnotationAspectJExpressionPointcutAdvisorManager();
            beanFactory.registerSingleton(managerName, manager);
        }
        return manager;
    }

    // 确定优先级
    private int determineAdviceOrder(Method method) {
        // 解析 @Order
        Order orderAnn = method.getAnnotation(Order.class);
        return orderAnn == null ? Ordered.LOWEST_PRECEDENCE : orderAnn.value();
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    private static class Point implements AdvicePoint {
        private final Object aspectBean;
        private final Method enhance;
        private final int    order;

        public Point(Object aspectBean, Method enhance, int order) {
            this.aspectBean = aspectBean;
            this.enhance = enhance;
            this.order = order;
        }

        @Override
        public int getOrder() {
            return order;
        }

        @Override
        public Object proceed(AdviceChain chain) throws Throwable {
            return enhance.invoke(aspectBean, chain);
        }
    }
}
