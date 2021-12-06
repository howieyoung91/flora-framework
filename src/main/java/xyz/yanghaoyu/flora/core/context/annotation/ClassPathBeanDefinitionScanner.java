package xyz.yanghaoyu.flora.core.context.annotation;

import xyz.yanghaoyu.flora.annotation.Component;
import xyz.yanghaoyu.flora.annotation.Configuration;
import xyz.yanghaoyu.flora.annotation.Scope;
import xyz.yanghaoyu.flora.core.beans.factory.config.BeanDefinition;
import xyz.yanghaoyu.flora.core.beans.factory.support.BeanDefinitionRegistry;
import xyz.yanghaoyu.flora.exception.BeansException;
import xyz.yanghaoyu.flora.exception.DuplicateDeclarationException;
import xyz.yanghaoyu.flora.util.StringUtil;

import java.util.Set;

public class ClassPathBeanDefinitionScanner extends ClassPathScanningCandidateComponentProvider {

    private final BeanDefinitionRegistry registry;

    public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    public void doScan(String... basePackages) {
        for (String basePackage : basePackages) {
            Set<Class<?>> candidateClass = findCandidateComponents(basePackage);
            for (Class<?> aClass : candidateClass) {
                BeanDefinition beanDefinition = new BeanDefinition(aClass);
                determineBeanScope(aClass, beanDefinition);

                String beanName = determineBeanName(beanDefinition);
                if (registry.containsBeanDefinition(beanName)) {
                    throw new BeansException("Duplicate beanId [" + beanName + "] is not allowed");
                }
                registry.registerBeanDefinition(beanName, beanDefinition);
            }
        }
    }

    private void determineBeanScope(Class<?> aClass, BeanDefinition beanDefinition) {
        Scope.Singleton singletonAnno = aClass.getAnnotation(Scope.Singleton.class);
        Scope.Prototype prototypeAnno = aClass.getAnnotation(Scope.Prototype.class);
        if (singletonAnno == null) {
            if (prototypeAnno != null) {
                beanDefinition.setScope(Scope.PROTOTYPE);
            }
        } else {
            if (prototypeAnno == null) {
                beanDefinition.setScope(Scope.SINGLETON);
            } else {
                throw new DuplicateDeclarationException("there are two [Scope] annotation at " + aClass.getSimpleName());
            }
        }

    }

    private String determineBeanName(BeanDefinition beanDefinition) {
        Class<?> beanClass = beanDefinition.getBeanClass();
        Component componentAnn = beanClass.getAnnotation(Component.class);
        Configuration configAnn = beanClass.getAnnotation(Configuration.class);
        if (componentAnn != null && configAnn != null) {
            throw new DuplicateDeclarationException("the duplicate declaration [@Component] and [@Configuration] on " + beanDefinition.getBeanClass());
        }
        String value = null;
        if (componentAnn != null) {
            value = componentAnn.value();
        } else if (configAnn != null) {
            value = configAnn.value();
        } else {
            throw new RuntimeException();
        }
        // 为空默认把首字母小写作为 id
        if (StringUtil.isEmpty(value)) {
            value = StringUtil.lowerFirstChar(beanClass.getSimpleName());
        }
        return value;
    }
}
