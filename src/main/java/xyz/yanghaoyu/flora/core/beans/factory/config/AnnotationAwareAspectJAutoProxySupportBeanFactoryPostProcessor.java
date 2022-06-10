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

import java.lang.annotation.Annotation;
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
        AnnotationAspectJExpressionPointcutAdvisorManager manager = null;
        if (beanFactory.containsSingletonBean(AnnotationAspectJExpressionPointcutAdvisorManager.class.getName())) {
            manager = beanFactory.getBean(AnnotationAspectJExpressionPointcutAdvisorManager.class.getName(), AnnotationAspectJExpressionPointcutAdvisorManager.class);
        }
        else {
            manager = new AnnotationAspectJExpressionPointcutAdvisorManager();
            beanFactory.registerSingleton(AnnotationAspectJExpressionPointcutAdvisorManager.class.getName(), manager);
        }


        String[] names = beanFactory.getBeanDefinitionNames();
        for (String name : names) {
            // 首先 生成 Aspect
            Class<?> clazz = beanFactory.getBeanDefinition(name).getBeanClass();
            // 是否标记 @Aspect
            Annotation annotation = clazz.getAnnotation(Aspect.class);
            if (annotation != null) {
                Object   bean    = beanFactory.getBean(name);
                Method[] methods = clazz.getDeclaredMethods();

                // 查找被 @Enhance 标记的方法
                for (Method method : methods) {
                    Aop.Enhance enhanceAnno = method.getAnnotation(Aop.Enhance.class);
                    if (enhanceAnno == null) {
                        continue;
                    }

                    LOGGER.trace("register [Pointcut] [{}]", enhanceAnno.value());
                    manager.addMethodEnhanceAdvice(
                            enhanceAnno.value(),
                            new Point(bean, method, getAdviceOrder(method))
                    );
                }
            }
        }
    }

    // 确定优先级
    private int getAdviceOrder(Method method) {
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
