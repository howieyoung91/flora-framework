/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.framework.core.context.event;

import xyz.yanghaoyu.flora.framework.core.context.ApplicationListener;

/**
 * @author <a href="https://www.yanghaoyu.xyz">Howie Young</a><i>on 2021/8/11 20:26<i/>
 * @version 1.0
 */


public interface ApplicationEventMulticaster {
    /**
     * 添加监听器
     */
    void addApplicationListener(ApplicationListener<?> listener);

    /**
     * 移除监听器
     */
    void removeApplicationListener(ApplicationListener<?> listener);

    /**
     * 广播监听器
     */
    void multicastEvent(ApplicationEvent event);
}
