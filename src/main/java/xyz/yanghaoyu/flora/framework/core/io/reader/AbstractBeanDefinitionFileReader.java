/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.framework.core.io.reader;

import xyz.yanghaoyu.flora.framework.core.beans.factory.support.BeanDefinitionRegistry;
import xyz.yanghaoyu.flora.framework.core.io.ResourceLoader;


public abstract class AbstractBeanDefinitionFileReader
        extends AbstractBeanDefinitionReader implements BeanDefinitionFileReader {
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
