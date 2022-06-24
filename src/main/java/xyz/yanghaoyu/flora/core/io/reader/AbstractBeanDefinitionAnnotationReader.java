package xyz.yanghaoyu.flora.core.io.reader;

import xyz.yanghaoyu.flora.core.beans.factory.support.BeanDefinitionRegistry;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/12/6 20:22<i/>
 * @version 1.0
 */


public abstract class AbstractBeanDefinitionAnnotationReader
        extends AbstractBeanDefinitionReader implements BeanDefinitionAnnotationReader {
    public AbstractBeanDefinitionAnnotationReader(BeanDefinitionRegistry registry) {
        super(registry);
    }
}
