/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.framework.core.context.support;

import xyz.yanghaoyu.flora.framework.core.beans.factory.support.DefaultListableBeanFactory;
import xyz.yanghaoyu.flora.framework.core.io.reader.AnnotationBeanDefinitionReader;

import java.io.IOException;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/12/6 20:08<i/>
 * @version 1.0
 */
public abstract class AbstractAnnotationConfigApplicationContext
        extends AbstractRefreshableAnnotationConfigApplicationContext {
    public AbstractAnnotationConfigApplicationContext(Class... classes) {
        setBaseConfigurationClasses(classes);
    }

    @Override
    protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) {
        AnnotationBeanDefinitionReader reader = new AnnotationBeanDefinitionReader(beanFactory);
        try {
            reader.loadBeanDefinitions(getBaseConfigurationClasses());
        }
        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
