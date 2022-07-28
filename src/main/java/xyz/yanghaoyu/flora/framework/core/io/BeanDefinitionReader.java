/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.framework.core.io;

import xyz.yanghaoyu.flora.framework.core.beans.factory.support.BeanDefinitionRegistry;

/**
 * @author <a href="https://www.yanghaoyu.xyz">Howie Young</a><i>on 2021/8/8 11:07<i/>
 * @version 1.0
 */


public interface BeanDefinitionReader {
    BeanDefinitionRegistry getRegistry();
}
