package xyz.yanghaoyu.flora.beans.factory;

/**
 * @author <a href="https://www.yanghaoyu.xyz">Howie Young</a><i>on 2021/8/11 11:11<i/>
 * @version 1.0
 */


public interface FactoryBean<T> {
    T getObject() throws Exception;

    Class<?> getObjectType();

    boolean isSingleton();
}
