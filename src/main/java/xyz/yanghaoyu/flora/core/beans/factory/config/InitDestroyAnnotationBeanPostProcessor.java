package xyz.yanghaoyu.flora.core.beans.factory.config;

import cn.hutool.core.bean.BeanException;
import xyz.yanghaoyu.flora.annotation.Life;
import xyz.yanghaoyu.flora.core.Ordered;
import xyz.yanghaoyu.flora.exception.BeanCreateException;
import xyz.yanghaoyu.flora.exception.BeansException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2022/2/4 19:22<i/>
 * @version 1.0
 */

public class InitDestroyAnnotationBeanPostProcessor
        implements DestructionAwareBeanPostProcessor, Ordered {
    private final Map<Class<?>, List[]> lifecycleMethodsCache = new HashMap<>();

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?>     beanClass        = bean.getClass();
        List[]       lifecycleMethods = findLifecycleMethod(beanClass);
        List<Method> initMethods      = lifecycleMethods[0];
        try {
            invokeInitialMethods(bean, initMethods);
        } catch (InvocationTargetException e) {
            throw new BeanCreateException(beanName, "Invocation of init method failed", e.getTargetException());
        } catch (IllegalAccessException e) {
            throw new BeanCreateException(beanName, "Invocation of init method failed", e);
        }
        return bean;
    }

    @Override
    public void postProcessBeforeDestruction(Object bean, String beanName) throws BeanException {
        Class<?>     beanClass        = bean.getClass();
        List[]       lifecycleMethods = findLifecycleMethod(beanClass);
        List<Method> destroyMethods   = lifecycleMethods[1];
        try {
            invokeDestroyMethods(bean, destroyMethods);
        } catch (InvocationTargetException e) {
            throw new BeanCreateException(beanName, "Invocation of destroy method failed", e.getTargetException());
        } catch (IllegalAccessException e) {
            throw new BeanCreateException(beanName, "Invocation of destroy method failed", e);
        }
    }

    private void invokeInitialMethods(Object bean, List<Method> initMethods) throws InvocationTargetException, IllegalAccessException {
        for (Method initMethod : initMethods) {
            initMethod.invoke(bean);
        }
    }

    private void invokeDestroyMethods(Object bean, List<Method> destroyMethods) throws InvocationTargetException, IllegalAccessException {
        for (Method destroyMethod : destroyMethods) {
            destroyMethod.invoke(bean);
        }
    }

    private List[] findLifecycleMethod(final Class<?> clazz) {
        List[] res = lifecycleMethodsCache.get(clazz);
        if (res != null) {
            return res;
        }

        ArrayList<Method> initMethods    = new ArrayList<>();
        ArrayList<Method> destroyMethods = new ArrayList<>();
        Class<?>          targetClass    = clazz;
        do {
            Method[] methods = targetClass.getDeclaredMethods();
            for (Method method : methods) {
                if (isInitMethod(method)) {
                    initMethods.add(0, method);
                }
                if (isDestroyMethod(method)) {
                    destroyMethods.add(method);
                }
            }
            // 向上查找
            targetClass = targetClass.getSuperclass();
        } while (targetClass != null && targetClass != Object.class);
        List[] methods = {initMethods, destroyMethods};
        lifecycleMethodsCache.put(clazz, methods);
        return methods;
    }

    private boolean isDestroyMethod(Method method) {
        return method.isAnnotationPresent(PreDestroy.class) || method.isAnnotationPresent(Life.Destroy.class);
    }

    private boolean isInitMethod(Method method) {
        return method.isAnnotationPresent(PostConstruct.class) || method.isAnnotationPresent(Life.Initialize.class);
    }

}
