package xyz.yanghaoyu.flora.core.beans.factory.support;

import xyz.yanghaoyu.flora.exception.BeansException;
import xyz.yanghaoyu.flora.core.beans.factory.FactoryBean;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 对 FactoryBean 做了支持 主要是对 FactoryBean 生成的 bean 增加了缓存
 */
public abstract class FactoryBeanRegistrySupport extends DefaultSingletonBeanRegistry {
    private final Map<String, Object> factoryBeanObjectCache = new ConcurrentHashMap<>();

    protected Object getCachedObjectForFactoryBean(String beanName) {
        Object object = this.factoryBeanObjectCache.get(beanName);
        return (object == NULL_OBJECT ? null : object);
    }

    protected Object getObjectFromFactoryBean(FactoryBean factoryBean, String beanName) {
        if (factoryBean.isSingleton()) {
            // 先看缓存中有没有
            Object object = this.factoryBeanObjectCache.get(beanName);
            if (object == null) {
                // 不存在就创建 然后放入缓存
                object = doGetObjectFromFactoryBean(factoryBean, beanName);
                this.factoryBeanObjectCache.put(beanName, (object == null ? NULL_OBJECT : object));
            }
            return (object == NULL_OBJECT ? null : object);
        } else {
            // 不是单例每次都要创建
            return doGetObjectFromFactoryBean(factoryBean, beanName);
        }
    }

    private Object doGetObjectFromFactoryBean(final FactoryBean factoryBean, final String beanName) {
        try {
            return factoryBean.getObject();
        } catch (Exception e) {
            throw new BeansException("FactoryBean threw exception on object[" + beanName + "] creation", e);
        }
    }
}
