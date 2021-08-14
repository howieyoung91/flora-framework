package xyz.yanghaoyu.flora.core.ioc.scanner;

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
        this.targetAnnotationClass = annotationClass;
    }

    @Override
    public boolean checkAddClass(Class<?> clazz) {
        if (targetAnnotationClass == null) {
            throw new NullPointerException();
        }
        return clazz.isAnnotationPresent(targetAnnotationClass);
    }
}
