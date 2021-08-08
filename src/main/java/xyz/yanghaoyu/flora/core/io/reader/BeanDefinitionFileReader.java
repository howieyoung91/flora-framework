package xyz.yanghaoyu.flora.core.io.reader;

import xyz.yanghaoyu.flora.core.io.BeanDefinitionReader;
import xyz.yanghaoyu.flora.core.io.Resource;
import xyz.yanghaoyu.flora.core.io.ResourceLoader;
import xyz.yanghaoyu.flora.exception.BeansException;

import java.io.IOException;

public interface BeanDefinitionFileReader extends BeanDefinitionReader {
    ResourceLoader getResourceLoader();

    void loadBeanDefinitions(Resource resource) throws BeansException, IOException, ClassNotFoundException;

    void loadBeanDefinitions(Resource... resources) throws BeansException, IOException;

    void loadBeanDefinitions(String location) throws BeansException, IOException;

    void loadBeanDefinitions(String... locations) throws BeansException, IOException;
}
