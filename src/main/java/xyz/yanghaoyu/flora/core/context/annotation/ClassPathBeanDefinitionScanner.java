package xyz.yanghaoyu.flora.core.context.annotation;

import xyz.yanghaoyu.flora.core.beans.factory.config.BeanDefinition;
import xyz.yanghaoyu.flora.util.ComponentUtil;

import java.util.HashSet;
import java.util.Set;

public class ClassPathBeanDefinitionScanner extends ClassPathScanningCandidateComponentProvider {

    public ClassPathBeanDefinitionScanner() {}


    public Set<BeanDefinition> doScan(String... basePackages) {
        Set<BeanDefinition> bds = new HashSet<>();
        for (String basePackage : basePackages) {
            Set<Class<?>> candidateClass = findCandidateComponents(basePackage);
            for (Class<?> aClass : candidateClass) {
                BeanDefinition beanDefinition = new BeanDefinition(aClass);
                ComponentUtil.determineBeanScope(beanDefinition);
                ComponentUtil.determineBeanInitMethodAndDestroyMethod(beanDefinition);
                bds.add(beanDefinition);
            }
        }
        return bds;
    }
}
