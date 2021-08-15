package xyz.yanghaoyu.flora.context.annotation;

import java.lang.annotation.Annotation;

/**
 * 注解类扫描器
 *
 * @author <a href="https://www.yanghaoyu.xyz">Howie Young</a><i>on 2021/4/27 22:50<i/>
 * @version 1.0
 */

public class AnnotationClassScanner extends ClassScanner {
    protected Class<? extends Annotation> targetAnnotationClass;

    public AnnotationClassScanner(String targetPackage, Class<? extends Annotation> annotationClass) {
        super(targetPackage);
        if (annotationClass == null) {
            throw new NullPointerException();
        }
        this.targetAnnotationClass = annotationClass;
    }

    @Override
    public boolean checkAddClass(Class<?> clazz) {
        return clazz.isAnnotationPresent(targetAnnotationClass);
    }
}
