package xyz.yanghaoyu.flora.core.io.reader;

import xyz.yanghaoyu.flora.BeansException;
import xyz.yanghaoyu.flora.beans.factory.support.BeanDefinitionRegistry;
import xyz.yanghaoyu.flora.core.io.loader.ResourceLoader;
import xyz.yanghaoyu.flora.core.io.resource.Resource;

/**
 * @author <a href="https://www.yanghaoyu.xyz">Howie Young</a><i>on 2021/8/8 11:12<i/>
 * @version 1.0
 */


public class YamlBeanDefinitionReader extends AbstractBeanDefinitionFileReader {
    protected YamlBeanDefinitionReader(BeanDefinitionRegistry registry) {
        super(registry);
    }

    public YamlBeanDefinitionReader(BeanDefinitionRegistry registry, ResourceLoader resourceLoader) {
        super(registry, resourceLoader);
    }

    @Override
    public void loadBeanDefinitions(Resource resource) throws BeansException {

    }

    @Override
    public void loadBeanDefinitions(Resource... resources) throws BeansException {

    }

    @Override
    public void loadBeanDefinitions(String location) throws BeansException {

    }
}
