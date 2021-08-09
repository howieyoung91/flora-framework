package xyz.yanghaoyu.flora.core.io.reader;

import xyz.yanghaoyu.flora.beans.factory.support.BeanDefinitionRegistry;

/**
 * 从注解读入配置
 *
 * @author <a href="https://www.yanghaoyu.xyz">Howie Young</a><i>on 2021/8/8 15:37<i/>
 * @version 1.0
 */

public abstract class AbstractBeanDefinitionAnnotationReader extends AbstractBeanDefinitionReader implements BeanDefinitionAnnotationReader {
    public AbstractBeanDefinitionAnnotationReader(BeanDefinitionRegistry registry) {
        super(registry);
    }
}
