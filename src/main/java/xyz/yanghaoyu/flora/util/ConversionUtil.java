package xyz.yanghaoyu.flora.util;

import cn.hutool.core.util.TypeUtil;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;
import xyz.yanghaoyu.flora.core.beans.factory.ConfigurableListableBeanFactory;
import xyz.yanghaoyu.flora.core.convert.converter.ConversionService;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Objects;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/11/18 17:05<i/>
 * @version 1.0
 */

public abstract class ConversionUtil {
    public static Class<?> getEnumType(Class<?> targetType) {
        Class<?> enumType = targetType;
        while (enumType != null && !enumType.isEnum()) {
            enumType = enumType.getSuperclass();
        }
        Objects.requireNonNull(enumType, () -> "The target type " + targetType.getName() + " does not refer to an enum");
        return enumType;
    }

    public static Object convertField(Field field, Object bean, ConfigurableListableBeanFactory beanFactory) {
        Class<?> sourceType = bean.getClass();
        // Class<?> targetType = (Class<?>) TypeUtil.getType(field);
        Class<?> targetType;
        Type fieldType = TypeUtil.getType(field);
        if (fieldType instanceof ParameterizedTypeImpl) {
            targetType = ((ParameterizedTypeImpl) fieldType).getRawType();
        } else {
            targetType = ((Class<?>) fieldType);
        }
        return convert(bean, beanFactory, sourceType, targetType);
    }

    public static Object convert(Object value, ConfigurableListableBeanFactory beanFactory, Class<?> sourceType, Class<?> targetType) {
        ConversionService conversionService = beanFactory.getConversionService();
        if (conversionService != null) {
            if (conversionService.canConvert(sourceType, targetType)) {
                value = conversionService.convert(value, targetType);
            }
        }
        return value;
    }

}
