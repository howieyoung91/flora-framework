package xyz.yanghaoyu.flora.core.io.scanner;

import xyz.yanghaoyu.flora.annotation.Component;
import xyz.yanghaoyu.flora.core.io.container.ClassContainer;

/**
 * 组件扫描器
 */
public class ComponentScanner extends AnnotationClassScanner {
    public ComponentScanner(String targetPackage) {
        super(targetPackage, Component.class);
    }

    public ComponentScanner(String targetPackage, ClassContainer classContainer) {
        super(targetPackage, classContainer, Component.class);
    }
}
