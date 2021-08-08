package xyz.yanghaoyu.flora.core.context.annotation;

import xyz.yanghaoyu.flora.annotation.Component;
import xyz.yanghaoyu.flora.annotation.Configuration;

import java.util.Set;

/**
 * @author <a href="https://www.yanghaoyu.xyz">Howie Young</a><i>on 2021/8/13 22:46<i/>
 * @version 1.0
 */


public class ClassPathScanningCandidateComponentProvider {
    protected Set<Class<?>> findCandidateComponents(String basePackage) {
        AnnotationClassScanner scanner =
                new AnnotationClassScanner(basePackage, Component.class, Configuration.class);
        scanner.scan();
        return scanner.getClassSet();
    }
}
