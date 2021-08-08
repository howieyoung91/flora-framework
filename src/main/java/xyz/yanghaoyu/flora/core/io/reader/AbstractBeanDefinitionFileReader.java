package xyz.yanghaoyu.flora.core.io.reader;

import xyz.yanghaoyu.flora.beans.factory.support.BeanDefinitionRegistry;
import xyz.yanghaoyu.flora.core.io.loader.ResourceLoader;


public abstract class AbstractBeanDefinitionFileReader extends AbstractBeanDefinitionReader implements BeanDefinitionFileReader {
    private ResourceLoader resourceLoader;

    public AbstractBeanDefinitionFileReader(BeanDefinitionRegistry registry) {
        super(registry);
    }

    public AbstractBeanDefinitionFileReader(BeanDefinitionRegistry registry, ResourceLoader resourceLoader) {
        super(registry);
        this.resourceLoader = resourceLoader;
    }

    @Override
    public ResourceLoader getResourceLoader() {
        return resourceLoader;
    }
}
