package xyz.yanghaoyu.flora.core.beans.factory.config;

import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.yanghaoyu.flora.annotation.Aop;
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
        implements BeanFactoryPostProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(AnnotationAwareAspectJAutoProxySupportBeanFactoryPostProcessor.class);

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        LOGGER.trace("init Aspect Bean in [AnnotationAwareAspectJAutoProxySupportBeanFactoryPostProcessor]");
        AnnotationAspectJExpressionPointcutAdvisorManager manager;
        try {
            manager = beanFactory.getBean(AnnotationAspectJExpressionPointcutAdvisorManager.class.getName(), AnnotationAspectJExpressionPointcutAdvisorManager.class);
        } catch (BeansException e) {
            manager = new AnnotationAspectJExpressionPointcutAdvisorManager();
            beanFactory.registerSingleton(AnnotationAspectJExpressionPointcutAdvisorManager.class.getName(), manager);
        }
        String[] names = beanFactory.getBeanDefinitionNames();
        for (String name : names) {
            // 首先 生成 Aspect
            Class clazz = beanFactory.getBeanDefinition(name).getBeanClass();
            Annotation annotation = clazz.getAnnotation(Aspect.class);
            if (annotation != null) {
                Object bean = beanFactory.getBean(name);
                Method[] methods = clazz.getDeclaredMethods();
                for (Method method : methods) {
                    Aop.Enhance enhanceAnno = method.getAnnotation(Aop.Enhance.class);
                    if (enhanceAnno == null) {
                        continue;
                    }
                    manager.addMethodEnhanceAdvice(enhanceAnno.pointcut(), new AdvicePoint() {
                        @Override
                        public int getPriority() {
                            return enhanceAnno.priority();
                        }

                        @Override
                        public Object proceed(AdviceChain chain) throws Throwable {
                            // 调用用户的方法
                            return method.invoke(bean, chain);
                        }
                    });
                }
            }
        }
    }
}
