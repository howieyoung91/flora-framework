package xyz.yanghaoyu.flora.core.beans.factory.config;

import cn.hutool.core.bean.BeanException;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2022/2/4 23:05<i/>
 * @version 1.0
 */

public interface DestructionAwareBeanPostProcessor extends BeanPostProcessor {
    void postProcessBeforeDestruction(Object bean, String beanName) throws BeanException;

    default boolean requiresDestruction(Object bean) {
        return true;
    }
}
