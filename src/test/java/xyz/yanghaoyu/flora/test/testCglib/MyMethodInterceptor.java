package xyz.yanghaoyu.flora.test.testCglib;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class MyMethodInterceptor implements MethodInterceptor {
    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        // System.out.println(method);
        System.out.println(method.getName());
        // System.out.println("obj.getClass() = " + obj);
        return proxy.invokeSuper(obj, args);
    }
}