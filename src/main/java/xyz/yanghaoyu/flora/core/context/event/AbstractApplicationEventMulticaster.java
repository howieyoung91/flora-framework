package xyz.yanghaoyu.flora.core.context.event;

import xyz.yanghaoyu.flora.core.beans.factory.BeanFactory;
import xyz.yanghaoyu.flora.core.beans.factory.BeanFactoryAware;
import xyz.yanghaoyu.flora.core.context.ApplicationListener;
import xyz.yanghaoyu.flora.exception.BeansException;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * 抽象事件广播器
 * 事件广播器中绑定了许多事件监听器, 一旦某个事件发生( multicast 被调用 ), 则会把事件交给与该事件的相关监听器处理
 *
 * @author <a href="https://www.yanghaoyu.xyz">Howie Young</a><i>on 2021/8/11 20:45<i/>
 * @version 1.0
 */

public abstract class AbstractApplicationEventMulticaster implements ApplicationEventMulticaster, BeanFactoryAware {
    public final Set<ApplicationListener> applicationListeners = new LinkedHashSet<>();
    private      BeanFactory              beanFactory;

    @Override
    public void addApplicationListener(ApplicationListener<?> listener) {
        applicationListeners.add(listener);
    }

    @Override
    public void removeApplicationListener(ApplicationListener<?> listener) {
        applicationListeners.remove(listener);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    /**
     * 获得与 event 相关的监听器
     */
    protected Collection<ApplicationListener> getApplicationListeners(ApplicationEvent event) {
        LinkedList<ApplicationListener> listeners = new LinkedList<>();
        for (ApplicationListener<ApplicationEvent> listener : applicationListeners) {
            if (supportsEvent(listener, event)) {
                listeners.add(listener);
            }
        }
        return listeners;
    }

    /**
     * 监听器是否对该事件感兴趣
     */
    protected boolean supportsEvent(ApplicationListener<ApplicationEvent> applicationListener, ApplicationEvent event) {
        Class<? extends ApplicationListener> listenerClass = applicationListener.getClass();

        // 按照 CglibSubclassingInstantiationStrategy、SimpleInstantiationStrategy 不同的实例化类型，需要判断后获取目标 class
        // Class<?> targetClass = ClassUtils.isCglibProxyClass(listenerClass) ? listenerClass.getSuperclass() : listenerClass;
        // Type genericInterface = targetClass.getGenericInterfaces()[0];


        Type     genericInterface   = listenerClass.getGenericInterfaces()[0];
        Type     actualTypeArgument = ((ParameterizedType) genericInterface).getActualTypeArguments()[0];
        String   className          = actualTypeArgument.getTypeName();
        Class<?> eventClass;
        try {
            eventClass = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new BeansException("wrong event class name: " + className);
        }
        return eventClass.isAssignableFrom(event.getClass());
    }


    // multicastEvent 交给子类实现
}
