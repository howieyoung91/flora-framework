/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.framework.core.context.event;

import xyz.yanghaoyu.flora.framework.core.beans.factory.BeanFactory;
import xyz.yanghaoyu.flora.framework.core.beans.factory.BeanFactoryAware;
import xyz.yanghaoyu.flora.framework.core.context.ApplicationListener;
import xyz.yanghaoyu.flora.framework.exception.BeansException;
import xyz.yanghaoyu.flora.framework.util.ReflectUtil;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

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
        Class<?> targetClass       = ReflectUtil.getBeanClassFromCglibProxyIfNecessary(applicationListener.getClass());
        Type[]   genericInterfaces = targetClass.getGenericInterfaces();
        Type     genericInterface  = genericInterfaces[0];

        Class<?> eventClass = null;
        String   className  = null;
        if ((genericInterface instanceof ParameterizedType)) {
            Type actualTypeArgument = ((ParameterizedType) genericInterface).getActualTypeArguments()[0];
            className = actualTypeArgument.getTypeName();
        }
        else if (genericInterface instanceof Class) {
            throw new UnsupportedOperationException("does not support inner class!");
            // todo  inner class
            // System.out.println(Arrays.toString(((Class<?>) genericInterface).getTypeParameters()));
            // genericInterface.get1
            // System.out.println(((Class<?>) genericInterface).getComponentType());
            // System.out.println(Arrays.toString(((Class<?>) genericInterface).getGenericInterfaces()));
        }

        try {
            eventClass = Class.forName(className);
        }
        catch (ClassNotFoundException e) {
            throw new BeansException("wrong event class name: " + className);
        }
        return eventClass.isAssignableFrom(event.getClass());
    }


    // multicastEvent 交给子类实现
}
