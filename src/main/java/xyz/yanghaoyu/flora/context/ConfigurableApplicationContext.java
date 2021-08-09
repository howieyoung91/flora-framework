package xyz.yanghaoyu.flora.context;

import xyz.yanghaoyu.flora.BeansException;


public interface ConfigurableApplicationContext extends ApplicationContext {
    /**
     * 刷新容器
     */
    void refresh() throws BeansException;
}
