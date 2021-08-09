package xyz.yanghaoyu.flora.core.io.reader;

import xyz.yanghaoyu.flora.BeansException;

/**
 * 从注解读入BeanDefinition
 */
public interface BeanDefinitionAnnotationReader extends BeanDefinitionReader {
    void loadBeanDefinitions(String pkg) throws BeansException;
}
