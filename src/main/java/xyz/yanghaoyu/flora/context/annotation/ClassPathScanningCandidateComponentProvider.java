package xyz.yanghaoyu.flora.context.annotation;

import xyz.yanghaoyu.flora.annotation.Component;

import java.util.Set;

/**
 * @author <a href="https://www.yanghaoyu.xyz">Howie Young</a><i>on 2021/8/13 22:46<i/>
 * @version 1.0
 */


public class ClassPathScanningCandidateComponentProvider {
    public Set<Class<?>> findCandidateComponents(String basePath) {
        AnnotationClassScanner scanner = new AnnotationClassScanner(basePath, Component.class);
        scanner.scan();
        return scanner.getClassSet();
    }
}
