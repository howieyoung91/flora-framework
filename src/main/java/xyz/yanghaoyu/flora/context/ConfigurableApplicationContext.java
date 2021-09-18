package xyz.yanghaoyu.flora.context;

import xyz.yanghaoyu.flora.exception.BeansException;

import java.lang.reflect.InvocationTargetException;


public interface ConfigurableApplicationContext extends ApplicationContext {
    /**
     * 刷新容器
     */
    void refresh() throws BeansException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException;

    void registerShutdownHook();

    void close();
}
