package xyz.yanghaoyu.flora.core.beans.factory.config;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import xyz.yanghaoyu.flora.annotation.Bean;
import xyz.yanghaoyu.flora.annotation.Component;
import xyz.yanghaoyu.flora.annotation.Inject;
import xyz.yanghaoyu.flora.core.beans.factory.ConfigurableListableBeanFactory;
import xyz.yanghaoyu.flora.core.beans.factory.support.DefaultListableBeanFactory;
import xyz.yanghaoyu.flora.exception.BeanCandidatesException;
import xyz.yanghaoyu.flora.exception.DuplicateDeclarationException;
import xyz.yanghaoyu.flora.util.ComponentUtil;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class ConfigurationBeanCglibMethodInterceptor implements MethodInterceptor {
    private final DefaultListableBeanFactory beanFactory;
    private final Set<Method>                cache = new HashSet(3);

    public ConfigurationBeanCglibMethodInterceptor(ConfigurableListableBeanFactory beanFactory) {
        this.beanFactory = (DefaultListableBeanFactory) beanFactory;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        if (!cache.contains(method)) {
            if (method.isAnnotationPresent(Bean.class)) {
                cache.add(method);
            }
        }
        if (cache.contains(method)) {
            String beanName = ComponentUtil.determineBeanName(method, method.getAnnotation(Bean.class));
            if (!beanFactory.isCurrentlyCreating(beanName)) {
                return beanFactory.getBean(beanName);
            }

            int i = 0;

            Parameter[] parameters = method.getParameters();
            for (Parameter parameter : parameters) {
                Inject.ByName byNameAnn = parameter.getAnnotation(Inject.ByName.class);
                Inject.ByType byTypeAnn = parameter.getAnnotation(Inject.ByType.class);
                if (byNameAnn != null && byTypeAnn != null) {
                    throw new DuplicateDeclarationException(
                            "duplicate declaration [@Inject.ByName],[@Inject.ByType] on "
                                    + parameter.getName()
                                    + " when creating bean [" + beanName + "]"
                    );
                }
                Object bean = getBeanFromBeanFactory(beanName, parameter, byNameAnn, byTypeAnn);
                args[i] = bean;
                i++;
            }
        }
        return methodProxy.invokeSuper(o, args);
    }

    private Object getBeanFromBeanFactory(String beanName, Parameter parameter, Inject.ByName byNameAnn, Inject.ByType byTypeAnn) {
        Object bean;
        if (byTypeAnn != null) {
            Class<?> type = parameter.getType();
            // FIXME 依赖于和 Bean 相同的类型 这里就会出现循环依赖
            Map<String, ?> candidate = beanFactory.getBeansOfType(type);
            if (candidate.size() == 0) {
                throw new BeanCandidatesException("find no candidate when creating bean [" + beanName + "]");
            }
            if (candidate.size() > 1) {
                throw new BeanCandidatesException("find too many candidates whose class is " + type + " when creating bean [" + beanName + "]");
            }
            bean = candidate.values().iterator().next();
        } else {
            String dependOnBeanName = Component.DEFAULT_BEAN_NAME;
            if (byNameAnn == null) {
                dependOnBeanName = parameter.getName();
            } else {
                dependOnBeanName = byNameAnn.value();
                if (Objects.equals(Component.DEFAULT_BEAN_NAME, dependOnBeanName)) {
                    dependOnBeanName = parameter.getName();
                }
            }
            bean = beanFactory.getBean(dependOnBeanName);
        }
        return bean;
    }
}