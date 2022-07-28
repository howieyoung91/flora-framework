/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.framework.core.context;

import xyz.yanghaoyu.flora.framework.core.beans.factory.ListableBeanFactory;
import xyz.yanghaoyu.flora.framework.core.beans.factory.config.AutowireCapableBeanFactory;
import xyz.yanghaoyu.flora.framework.core.io.ResourceLoader;

/**
 * @author <a href="https://www.yanghaoyu.xyz">Howie Young</a><i>on 2021/8/9 11:37<i/>
 * @version 1.0
 */
public interface ApplicationContext
        extends ListableBeanFactory, ResourceLoader, ApplicationEventPublisher {
    AutowireCapableBeanFactory getAutowireCapableBeanFactory();
}
