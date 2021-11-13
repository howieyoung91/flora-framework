package xyz.yanghaoyu.flora.beans.factory.config;

import xyz.yanghaoyu.flora.annotation.Aop;
import xyz.yanghaoyu.flora.annotation.Inject;
import xyz.yanghaoyu.flora.annotation.Value;
import xyz.yanghaoyu.flora.aop.aspectj.AnnotationAspectJExpressionPointcutAdvisorManager;
import xyz.yanghaoyu.flora.aop.interceptor.MethodChain;
import xyz.yanghaoyu.flora.aop.interceptor.MethodPoint;
import xyz.yanghaoyu.flora.beans.factory.BeanFactory;
import xyz.yanghaoyu.flora.beans.factory.BeanFactoryAware;
import xyz.yanghaoyu.flora.beans.factory.ConfigurableListableBeanFactory;
import xyz.yanghaoyu.flora.beans.factory.PropertyValues;
import xyz.yanghaoyu.flora.exception.BeansException;
import xyz.yanghaoyu.flora.exception.DuplicateDeclarationException;
import xyz.yanghaoyu.flora.util.ReflectUtil;
import xyz.yanghaoyu.flora.util.StringUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

public class AutowiredAnnotationBeanPostProcessor implements InstantiationAwareBeanPostProcessor, BeanFactoryAware {
    private ConfigurableListableBeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
    }

    @Override
    public PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName) throws BeansException {
        Class<?> clazz = bean.getClass();
        clazz = ReflectUtil.isCglibProxyClass(clazz) ? clazz.getSuperclass() : clazz;
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            // 1. 处理注解 @Value
            handleValueAnnotation(bean, field);
            // 2. 处理注解 @Inject.ByType 和 @Inject.ByName
            handleInjectAnnotation(bean, beanName, field);
        }


        // TODO
        AnnotationAspectJExpressionPointcutAdvisorManager manager = getAnnotationAdvisorManager();
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            Aop.Enhance enhanceAnno = method.getAnnotation(Aop.Enhance.class);
            if (enhanceAnno == null) {
                continue;
            }
            manager.addMethodEnhanceAdvice(enhanceAnno.value(), new MethodPoint() {
                @Override
                public int getPriority() {
                    return enhanceAnno.priority();
                }

                @Override
                public Object proceed(MethodChain chain) throws Throwable {
                    // 调用用户的方法
                    return method.invoke(bean, chain);
                }
            });
        }

        return null;
    }

    private AnnotationAspectJExpressionPointcutAdvisorManager getAnnotationAdvisorManager() {
        AnnotationAspectJExpressionPointcutAdvisorManager advisor;
        try {
            advisor = beanFactory.getBean(AnnotationAspectJExpressionPointcutAdvisorManager.class.getName(), AnnotationAspectJExpressionPointcutAdvisorManager.class);
        } catch (BeansException e) {
            advisor = new AnnotationAspectJExpressionPointcutAdvisorManager();
            beanFactory.registerSingleton(AnnotationAspectJExpressionPointcutAdvisorManager.class.getName(), advisor);
        }
        return advisor;
    }

    private void handleInjectAnnotation(Object bean, String beanName, Field field) {
        Inject.ByType injectByTypeAnno = field.getAnnotation(Inject.ByType.class);
        Inject.ByName injectByNameAnno = field.getAnnotation(Inject.ByName.class);
        if (injectByNameAnno != null && injectByTypeAnno != null) {
            throw new DuplicateDeclarationException("the [@Inject.ByType] and [@Inject.ByName] are duplicate!");
        }
        // 处理 Inject.ByType
        if (injectByTypeAnno != null) {
            Map<String, ?> candidates = beanFactory.getBeansOfType(field.getType());
            if (candidates.size() == 1) {
                for (Object dependOnBean : candidates.values()) {
                    ReflectUtil.setFieldValue(bean, field.getName(), dependOnBean);
                }
            } else {
                if (injectByTypeAnno.required()) {
                    if (candidates.size() > 1) {
                        throw new BeansException("multiple beans are candidate! at: " + beanName + "field: " + field.getName() + ". Please use Inject.ByName");
                    } else {
                        throw new BeansException("No such Bean which class is  " + field.getType().getName());
                    }
                }
            }
        }

        // 处理 Inject.ByName
        if (injectByNameAnno != null) {
            String value = injectByNameAnno.value();
            String id = injectByNameAnno.id();
            if (StringUtil.isEmpty(id)) {
                id = StringUtil.isEmpty(value)
                        ? StringUtil.lowerFirstChar(field.getName())
                        : value;
            }
            Object dependOnBean = beanFactory.getBean(id);
            if (dependOnBean == null && injectByNameAnno.required()) {
                throw new BeansException("No such Bean which id is  " + id);
            }
            ReflectUtil.setFieldValue(bean, field.getName(), dependOnBean);
        }
    }

    private void handleValueAnnotation(Object bean, Field field) {
        Value valueAnnotation = field.getAnnotation(Value.class);
        if (null != valueAnnotation) {
            String value = valueAnnotation.value();
            String tempValue = beanFactory.resolveEmbeddedValue(value);
            if (tempValue == null && valueAnnotation.required()) {
                throw new NullPointerException();
            }
            value = tempValue;
            ReflectUtil.setFieldValue(bean, field.getName(), value);
        }
    }


    // public static class SampleClass {
    //     public void test() {
    //         System.out.println("hello world");
    //     }
    // }
    //
    // public static void main(String[] args) {
    //     Enhancer enhancer = new Enhancer();
    //     enhancer.setSuperclass(SampleClass.class);
    //     enhancer.setCallback(new MethodInterceptor() {
    //         @Override
    //         public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
    //             System.out.println("before method run...");
    //             Object result = proxy.invokeSuper(obj, args);
    //             System.out.println("after method run...");
    //             return result;
    //         }
    //     });
    //     SampleClass sample = (SampleClass) enhancer.create();
    //     sample.test();
    // }
}
