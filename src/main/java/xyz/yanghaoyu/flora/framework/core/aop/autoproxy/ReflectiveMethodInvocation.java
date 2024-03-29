/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.framework.core.aop.autoproxy;

import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;

/**
 * @author <a href="https://www.yanghaoyu.xyz">Howie Young</a><i>on 2021/8/12 18:11<i/>
 * @version 1.0
 */


public class ReflectiveMethodInvocation implements MethodInvocation {
    // 目标对象
    protected final Object   target;
    // 方法
    protected final Method   method;
    // 入参
    protected final Object[] arguments;
    protected final Object   proxy;

    public ReflectiveMethodInvocation(Object target, Object proxy, Method method, Object[] arguments) {
        this.target = target;
        this.method = method;
        this.arguments = arguments;
        this.proxy = proxy;
    }

    @Override
    public Method getMethod() {
        return method;
    }

    @Override
    public Object[] getArguments() {
        return arguments;
    }

    @Override
    public Object proceed() throws Throwable {
        return invokeObjectMethod();
    }

    public Object invokeObjectMethod() throws Throwable {
        return method.invoke(target, arguments);
    }

    @Override
    public Object getThis() {
        return target;
    }

    @Override
    public AccessibleObject getStaticPart() {
        return method;
    }

    public Object getProxy() {
        return proxy;
    }
}
