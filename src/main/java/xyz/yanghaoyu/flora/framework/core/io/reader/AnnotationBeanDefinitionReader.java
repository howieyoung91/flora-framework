/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.framework.core.io.reader;

import xyz.yanghaoyu.flora.framework.core.beans.factory.config.BeanDefinition;
import xyz.yanghaoyu.flora.framework.core.beans.factory.support.BeanDefinitionRegistry;
import xyz.yanghaoyu.flora.framework.exception.BeansException;
import xyz.yanghaoyu.flora.framework.util.ComponentUtil;
import xyz.yanghaoyu.flora.framework.util.IocUtil;

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
        IocUtil.enableAutowiredAnnotations(getRegistry());
        IocUtil.enableTypeConvert(getRegistry());
        addClass(configClasses);
    }

    private void addClass(Class[] configClasses) {
        for (Class<?> clazz : configClasses) {
            addClass(clazz);
        }
    }

    private void addClass(Class<?> clazz) {
        BeanDefinition beanDef = ComponentUtil.parse(clazz);
        if (beanDef != null) {
            String beanName = ComponentUtil.determineBeanName(beanDef);
            getRegistry().registerBeanDefinition(beanName, beanDef);
        }
        Class<?>[] innerClasses = clazz.getDeclaredClasses();
        addClass(innerClasses);
    }
}
