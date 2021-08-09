package xyz.yanghaoyu.flora.util;

import javax.annotation.*;
import java.lang.annotation.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * @author <a href="https://www.yanghaoyu.xyz">Howie Young</a><i>on 2021/8/7 22:40<i/>
 * @version 1.0
 */


public class ReflectUtil {
    private ReflectUtil() { }

    public static Object newInstanceByDefaultCtor(Class clazz) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        return clazz.getConstructor().newInstance();
    }

    public static Constructor selectCtorByArgsType(Class clazz, Object[] args) throws NoSuchMethodException {
        if (args == null || args.length == 0) {
            return clazz.getDeclaredConstructor();
        }
        Constructor[] ctors = clazz.getDeclaredConstructors();
        OUT:
        for (Constructor ctor : ctors) {
            Class[] parameterTypes = ctor.getParameterTypes();
            if (parameterTypes.length == args.length) {
                for (int i = 0; i < parameterTypes.length; i++) {
                    // 判断每一个参数类型是否相等
                    if (args[i].getClass() != parameterTypes[i]) {
                        continue OUT;
                    }
                }
                return ctor;
            }
        }
        throw new NoSuchMethodException("no such constructor!");
    }


    public static void setFieldValue(Object bean, String name, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field field = bean.getClass().getDeclaredField(name);
        field.setAccessible(true);
        field.set(bean, value);
    }

    public static ClassLoader getDefaultClassLoader() {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        } catch (Throwable ex) {
            // Cannot access thread context ClassLoader - falling back...
        }
        if (cl == null) {
            // No thread context class loader -> use class loader of this class.
            cl = ReflectUtil.class.getClassLoader();
            if (cl == null) {
                // getClassLoader() returning null indicates the bootstrap ClassLoader
                try {
                    cl = ClassLoader.getSystemClassLoader();
                } catch (Throwable ex) {
                    // Cannot access system ClassLoader - oh well, maybe the caller can live with null...
                }
            }
        }
        return cl;
    }

    public static Class<?> loadClass(String className, boolean isInitialized) {
        Class<?> clazz = null;
        try {
            clazz = Class.forName(className, isInitialized, getDefaultClassLoader());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return clazz;
    }

    public static Annotation getAnnotation(Class clazz, Class<? extends Annotation> targetAnno) {
        Annotation[] annotations = clazz.getAnnotations();
        for (Annotation annotation : annotations) {
            if (
                    annotation.annotationType() != Deprecated.class &&
                    annotation.annotationType() != SuppressWarnings.class &&
                    annotation.annotationType() != Override.class &&
                    annotation.annotationType() != PostConstruct.class &&
                    annotation.annotationType() != PreDestroy.class &&
                    annotation.annotationType() != Resource.class &&
                    annotation.annotationType() != Resources.class &&
                    annotation.annotationType() != Generated.class &&
                    annotation.annotationType() != Target.class &&
                    annotation.annotationType() != Retention.class &&
                    annotation.annotationType() != Documented.class &&
                    annotation.annotationType() != Inherited.class
            ) {
                if (
                        annotation.annotationType() == targetAnno
                ) {
                    return annotation;
                } else {
                    getAnnotation(annotation.annotationType(), targetAnno);
                }
            }
        }
        return null;
    }
}
