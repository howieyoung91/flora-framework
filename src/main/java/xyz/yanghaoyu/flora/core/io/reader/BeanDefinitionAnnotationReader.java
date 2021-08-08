package xyz.yanghaoyu.flora.core.io.reader;

import xyz.yanghaoyu.flora.BeansException;

import java.io.IOException;

/**
 * 从注解读入配置
 */
public interface BeanDefinitionAnnotationReader extends BeanDefinitionReader {
    void loadBeanDefinitions(String pkg) throws BeansException, IOException;
}
