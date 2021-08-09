package xyz.yanghaoyu.flora.core.io.scanner;

import xyz.yanghaoyu.flora.core.io.container.ClassContainer;

import java.lang.annotation.Annotation;

public class AnnotationClassScanner extends ClassScanner {
    protected Class<? extends Annotation> targetAnnotationClass;

    public AnnotationClassScanner(String targetPackage, Class<? extends Annotation> targetAnnotationClass) {
        super(targetPackage);
        this.targetAnnotationClass = targetAnnotationClass;
    }

    public AnnotationClassScanner(String targetPackage, ClassContainer classContainer, Class<? extends Annotation> targetAnnotationClass) {
        super(targetPackage, classContainer);
        this.targetAnnotationClass = targetAnnotationClass;
    }

    @Override
    public boolean checkAddClass(Class<?> cls) {
        if (cls == Annotation.class) {
            return false;
        }
        if (targetAnnotationClass == null) {
            throw new NullPointerException();
        }
        return cls.isAnnotationPresent(targetAnnotationClass);
    }
}
