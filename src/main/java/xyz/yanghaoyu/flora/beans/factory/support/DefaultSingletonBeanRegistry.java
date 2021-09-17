package xyz.yanghaoyu.flora.beans.factory.support;

import xyz.yanghaoyu.flora.beans.factory.ObjectFactory;
import xyz.yanghaoyu.flora.beans.factory.config.SingletonBeanRegistry;
import xyz.yanghaoyu.flora.exception.BeansException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {
    protected static final Object NULL_OBJECT = new Object();
    // 成品
    private Map<String, Object> singletonObjects = new ConcurrentHashMap<>();
    // 半成品
    protected final Map<String, Object> earlySingletonObjects = new HashMap<>();
    // 代理对象
    protected final Map<String, ObjectFactory<?>> singletonFactories = new HashMap<>();


    private Map<String, DisposableBean> disposableBeans = new HashMap<>();

    @Override
    public Object getSingleton(String beanName) {
        Object res = singletonObjects.get(beanName);
        if (res == null) {
            res = earlySingletonObjects.get(beanName);
            if (res == null) {
                ObjectFactory factory = singletonFactories.get(beanName);
                if (factory != null) {
                    res = factory.getObject();
                    // 从三级缓存中移除
                    singletonFactories.remove(beanName);
                    // 加入二级缓存
                    earlySingletonObjects.put(beanName, res);
                }
            }
        }

        return res;
    }

    public void registerSingleton(String beanName, Object singletonObject) {
        // 先从二级缓存和三级缓存中移除
        earlySingletonObjects.remove(beanName);
        singletonFactories.remove(beanName);
        singletonObjects.put(beanName, singletonObject);
    }

    protected void addSingletonFactory(String beanName, ObjectFactory<?> singletonFactory) {
        if (!this.singletonObjects.containsKey(beanName)) {
            this.singletonFactories.put(beanName, singletonFactory);
            this.earlySingletonObjects.remove(beanName);
        }
    }

    public void registerDisposableBean(String beanName, DisposableBean bean) {
        disposableBeans.put(beanName, bean);
    }

    /**
     * 在容器关闭时销毁bean
     */
    public void destroySingletons() {
        Set<String> keySet = this.disposableBeans.keySet();
        Object[] disposableBeanNames = keySet.toArray();

        for (int i = disposableBeanNames.length - 1; i >= 0; i--) {
            Object beanName = disposableBeanNames[i];
            DisposableBean disposableBean = disposableBeans.remove(beanName);
            try {
                disposableBean.destroy();
            } catch (Exception e) {
                throw new BeansException("Destroy method on bean with name '" + beanName + "' threw an exception", e);
            }
        }
    }
}
