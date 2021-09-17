package xyz.yanghaoyu.flora.beans.factory;

import xyz.yanghaoyu.flora.exception.BeansException;

/**
 * @author <a href="https://www.yanghaoyu.xyz">Howie Young</a><i>on 2021/9/17 19:56<i/>
 * @version 1.0
 */


public interface ObjectFactory<T> {
    T getObject() throws BeansException;
}
