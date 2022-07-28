/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.framework.core.io.reader;

import xyz.yanghaoyu.flora.framework.core.io.BeanDefinitionReader;
import xyz.yanghaoyu.flora.framework.exception.BeansException;

import java.io.IOException;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/12/6 20:25<i/>
 * @version 1.0
 */


public interface BeanDefinitionAnnotationReader extends BeanDefinitionReader {
    void loadBeanDefinitions(Class... configClasses) throws BeansException, IOException, ClassNotFoundException;
}
