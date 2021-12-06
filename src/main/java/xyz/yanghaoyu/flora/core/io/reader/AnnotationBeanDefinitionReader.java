package xyz.yanghaoyu.flora.core.io.reader;

import xyz.yanghaoyu.flora.core.beans.factory.config.BeanDefinition;
import xyz.yanghaoyu.flora.core.beans.factory.support.BeanDefinitionRegistry;
import xyz.yanghaoyu.flora.exception.BeansException;
import xyz.yanghaoyu.flora.util.ComponentUtil;
import xyz.yanghaoyu.flora.util.IocUtil;

import java.io.IOException;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/12/6 20:41<i/>
 * @version 1.0
 */


public class AnnotationBeanDefinitionReader extends AbstractBeanDefinitionAnnotationReader {
    public AnnotationBeanDefinitionReader(BeanDefinitionRegistry registry) {
        super(registry);
    }

    @Override
    public void loadBeanDefinitions(Class... configClasses) throws BeansException, IOException, ClassNotFoundException {
        IocUtil.enableComponentScan(getRegistry());
        for (Class<?> aClass : configClasses) {
            BeanDefinition beanDefinition = new BeanDefinition(aClass);
            ComponentUtil.determineBeanScope(beanDefinition);
            ComponentUtil.determineBeanInitMethodAndDestroyMethod(beanDefinition);
            String beanName = ComponentUtil.determineBeanName(beanDefinition);
            getRegistry().registerBeanDefinition(beanName, beanDefinition);
        }
    }
}
