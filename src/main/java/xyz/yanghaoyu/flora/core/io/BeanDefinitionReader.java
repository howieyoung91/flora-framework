package xyz.yanghaoyu.flora.core.io;

import xyz.yanghaoyu.flora.core.beans.factory.support.BeanDefinitionRegistry;

/**
 * @author <a href="https://www.yanghaoyu.xyz">Howie Young</a><i>on 2021/8/8 11:07<i/>
 * @version 1.0
 */


public interface BeanDefinitionReader {
    BeanDefinitionRegistry getRegistry();
}
