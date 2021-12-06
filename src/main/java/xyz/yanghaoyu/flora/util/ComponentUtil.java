package xyz.yanghaoyu.flora.util;

import xyz.yanghaoyu.flora.annotation.Component;
import xyz.yanghaoyu.flora.annotation.Configuration;
import xyz.yanghaoyu.flora.annotation.Scope;
import xyz.yanghaoyu.flora.core.beans.factory.config.BeanDefinition;
import xyz.yanghaoyu.flora.exception.DuplicateDeclarationException;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/12/6 13:19<i/>
 * @version 1.0
 */


public abstract class ComponentUtil {
    public static String determineComponentAnnBeanName(BeanDefinition beanDefinition) {
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

    public static void determineBeanScope(Class<?> aClass, BeanDefinition beanDefinition) {
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
}
