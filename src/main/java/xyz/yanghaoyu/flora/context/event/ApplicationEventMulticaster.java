package xyz.yanghaoyu.flora.context.event;

import xyz.yanghaoyu.flora.context.ApplicationListener;

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
