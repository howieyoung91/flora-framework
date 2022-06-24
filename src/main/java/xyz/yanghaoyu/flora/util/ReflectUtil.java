package xyz.yanghaoyu.flora.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Set;

import cn.hutool.core.lang.Assert;

/**
 * @author <a href="https://www.yanghaoyu.xyz">Howie Young</a><i>on 2021/8/7 22:40<i/>
 * @version 1.0
 */


public abstract class ReflectUtil {

    public static Object newInstanceByDefaultCtor(Class clazz) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        return clazz.getConstructor().newInstance();
    }

    public static Constructor selectConstructorByArgsType(Class clazz, Object[] args) throws NoSuchMethodException {
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

    public static void setFieldValue(Object bean, Class<?> clazz, String name, Object value) {
        Field field = null;
        try {
            field = clazz.getDeclaredField(name);
            field.setAccessible(true);
            field.set(bean, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void setFieldValue(Object bean, String name, Object value) {
        Field field = null;
        try {
            field = bean.getClass().getDeclaredField(name);
            field.setAccessible(true);
            field.set(bean, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
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

    // private static Set<Class> ANNS = new HashSet<>();

    public static Annotation getAnnotation(Class clazz, Set<Class<? extends Annotation>> targetAnns) {
        Annotation[] annotations = clazz.getAnnotations();
        for (Annotation annotation : annotations) {
            if (targetAnns.contains(annotation.annotationType())) {
                return annotation;
            } else {
                getAnnotation(annotation.annotationType(), targetAnns);
            }
        }
        return null;
    }

    public static Annotation getAnnotation(Class clazz, Class<? extends Annotation> targetAnn) {
        Annotation[] annotations = clazz.getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation.annotationType() == targetAnn) {
                return annotation;
            } else {
                getAnnotation(annotation.annotationType(), targetAnn);
            }
        }
        return null;
    }

    public static boolean belongToObjectClass(Method method) {
        return method.getDeclaringClass() == Object.class;
    }

    public static String getQualifiedMethodName(Method method, Class<?> clazz) {
        Assert.notNull(method, "Method must not be null");
        return (clazz != null ? clazz : method.getDeclaringClass()).getName() + "." + method.getName();
    }

    public static boolean isPublic(Method method) {
        return Modifier.isPublic(method.getModifiers());
    }

    /**
     * Check whether the specified class is a CGLIB-generated class.
     *
     * @param clazz the class to check
     */
    public static boolean isCglibProxyClass(Class<?> clazz) {
        return (clazz != null && isCglibProxyClassName(clazz.getName()));
    }

    /**
     * Check whether the specified class name is a CGLIB-generated class.
     *
     * @param className the class name to check
     */
    public static boolean isCglibProxyClassName(String className) {
        return (className != null && className.contains("$$"));
    }

    public static Class<?> getBeanClassFromCglibProxyIfNecessary(Class<?> clazz) {
        clazz = ReflectUtil.isCglibProxyClass(clazz) ? clazz.getSuperclass() : clazz;
        return clazz;
    }
}
