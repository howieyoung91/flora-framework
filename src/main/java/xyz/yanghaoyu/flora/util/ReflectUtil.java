package xyz.yanghaoyu.flora.util;

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
}
