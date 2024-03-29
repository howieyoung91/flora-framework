/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.framework.util;

import cn.hutool.core.util.TypeUtil;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;
import xyz.yanghaoyu.flora.framework.core.convert.converter.ConversionService;

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

    public static Object convertField(Field field, Object bean, ConversionService conversionService) {
        Class<?> sourceType = bean.getClass();
        // Class<?> targetType = (Class<?>) TypeUtil.getType(field);
        Class<?> targetType;
        Type fieldType = TypeUtil.getType(field);
        if (fieldType instanceof ParameterizedTypeImpl) {
            targetType = ((ParameterizedTypeImpl) fieldType).getRawType();
        } else {
            targetType = ((Class<?>) fieldType);
        }
        return convert(bean, targetType, sourceType, conversionService);
    }

    // @Deprecated
    // public static Object convertField(Field field, Object bean, ConfigurableListableBeanFactory beanFactory) {
    //     Class<?> sourceType = bean.getClass();
    //     // Class<?> targetType = (Class<?>) TypeUtil.getType(field);
    //     Class<?> targetType;
    //     Type fieldType = TypeUtil.getType(field);
    //     if (fieldType instanceof ParameterizedTypeImpl) {
    //         targetType = ((ParameterizedTypeImpl) fieldType).getRawType();
    //     } else {
    //         targetType = ((Class<?>) fieldType);
    //     }
    //     return convert(bean, targetType, sourceType, beanFactory.getConversionService());
    // }
    //
    // @Deprecated
    // public static Object convert(Object value, Class<?> targetType, Class<?> sourceType, ConfigurableListableBeanFactory beanFactory) {
    //     ConversionService conversionService = beanFactory.getConversionService();
    //     if (conversionService != null) {
    //         if (conversionService.canConvert(sourceType, targetType)) {
    //             value = conversionService.convert(value, targetType);
    //         }
    //     }
    //     return value;
    // }

    public static Object convert(Object value, Class<?> targetType, Class<?> sourceType, ConversionService conversionService) {
        if (conversionService != null) {
            if (conversionService.canConvert(sourceType, targetType)) {
                value = conversionService.convert(value, targetType);
            }
        }
        return value;
    }
}
