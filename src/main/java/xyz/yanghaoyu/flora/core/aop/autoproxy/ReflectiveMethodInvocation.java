package xyz.yanghaoyu.flora.core.aop.autoproxy;

import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;

/**
 * @author <a href="https://www.yanghaoyu.xyz">Howie Young</a><i>on 2021/8/12 18:11<i/>
 * @version 1.0
 */


public class ReflectiveMethodInvocation implements MethodInvocation {
    // List<MethodInterceptor> methodInterceptors;
    // int index = -1;
    // 目标对象
    protected final Object   target;
    // 方法
    protected final Method   method;
    // 入参
    protected final Object[] arguments;

    public ReflectiveMethodInvocation(Object target, Method method, Object[] arguments) {
        this.target = target;
        this.method = method;
        this.arguments = arguments;
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
        // Object res = null;
        // if (++index == methodInterceptors.size()) {
        //     res = invokeObjectMethod();
        // } else {
        //     MethodInterceptor methodInterceptor = methodInterceptors.get(index);
        //     res = methodInterceptor.invoke(this);
        // }
        // return res;
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
}
