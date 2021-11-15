package xyz.yanghaoyu.flora.beans.factory.config;

import org.aspectj.lang.annotation.Aspect;
import xyz.yanghaoyu.flora.annotation.Aop;
import xyz.yanghaoyu.flora.aop.aspectj.AnnotationAspectJExpressionPointcutAdvisorManager;
import xyz.yanghaoyu.flora.aop.interceptor.AdviceChain;
import xyz.yanghaoyu.flora.aop.interceptor.AdvicePoint;
import xyz.yanghaoyu.flora.beans.factory.ConfigurableListableBeanFactory;
import xyz.yanghaoyu.flora.exception.BeansException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/11/14 22:45<i/>
 * @version 1.0
 */


public class AnnotationAopSupport implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
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
