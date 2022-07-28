/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.framework.core.context.annotation;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 注解类扫描器
 *
 * @author <a href="https://www.yanghaoyu.xyz">Howie Young</a><i>on 2021/4/27 22:50<i/>
 * @version 1.0
 */
// @Deprecated
public class AnnotationClassScanner extends ClassScanner {
    protected Set<Class<? extends Annotation>> set = new HashSet<>(3);

    public AnnotationClassScanner(String targetPackage, Class<? extends Annotation>... annotationClass) {
        super(targetPackage);
        if (annotationClass == null) {
            throw new NullPointerException();
        }
        this.set.addAll(Arrays.asList(annotationClass));
    }

    @Override
    public boolean canAdd(Class<?> clazz) {
        for (Class<? extends Annotation> annClass : set) {
            boolean isPresent = clazz.isAnnotationPresent(annClass);
            if (isPresent) {
                return true;
            }
        }
        return false;
    }
}
