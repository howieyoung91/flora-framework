/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.core.beans.factory;

import xyz.yanghaoyu.flora.core.io.ResourceLoader;
import xyz.yanghaoyu.flora.exception.BeansException;

public interface ResourceLoaderAware extends Aware {
    void setResourceLoader(ResourceLoader resourceLoader) throws BeansException;
}
