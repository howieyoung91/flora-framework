/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.framework.core.io.reader;

import xyz.yanghaoyu.flora.framework.core.io.BeanDefinitionReader;
import xyz.yanghaoyu.flora.framework.core.io.Resource;
import xyz.yanghaoyu.flora.framework.core.io.ResourceLoader;
import xyz.yanghaoyu.flora.framework.exception.BeansException;

import java.io.IOException;

public interface BeanDefinitionFileReader extends BeanDefinitionReader {
    ResourceLoader getResourceLoader();

    void loadBeanDefinitions(Resource resource) throws BeansException, IOException, ClassNotFoundException;

    void loadBeanDefinitions(Resource... resources) throws BeansException, IOException;

    void loadBeanDefinitions(String location) throws BeansException, IOException;

    void loadBeanDefinitions(String... locations) throws BeansException, IOException;
}
