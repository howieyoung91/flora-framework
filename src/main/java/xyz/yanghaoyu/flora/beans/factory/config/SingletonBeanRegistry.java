package xyz.yanghaoyu.flora.beans.factory.config;

/**
 * @author <a href="https://www.yanghaoyu.xyz">Howie Young</a><i>on 2021/8/7 20:46<i/>
 * @version 1.0
 */

public interface SingletonBeanRegistry {
    Object getSingleton(String beanName);
}
