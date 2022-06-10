package xyz.yanghaoyu.flora.core.beans.factory.support;

import xyz.yanghaoyu.flora.exception.BeansException;
import xyz.yanghaoyu.flora.core.beans.factory.config.BeanDefinition;
import xyz.yanghaoyu.flora.util.ReflectUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class JDKInstantiationStrategy implements InstantiationStrategy {
    @Override
    public Object instantiate(BeanDefinition beanDefinition, Constructor constructor, Object[] args) {
        Class clazz = beanDefinition.getBeanClass();
        try {
            if (constructor == null || args == null || args.length == 0) {
                return ReflectUtil.newInstanceByDefaultCtor(clazz);
            }
            else {
                return constructor.newInstance(args);
            }
        }
        catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new BeansException("Failed to instantiate [" + clazz.getName() + "]", e);
        }
    }
}
