package xyz.yanghaoyu.flora.core.context;

import xyz.yanghaoyu.flora.exception.BeansException;


public interface ConfigurableApplicationContext extends ApplicationContext {
    /**
     * 刷新容器
     */
    void refresh() throws BeansException;

    void registerShutdownHook();

    void close();
}
