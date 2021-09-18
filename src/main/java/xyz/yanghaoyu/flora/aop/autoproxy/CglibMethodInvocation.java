package xyz.yanghaoyu.flora.aop.autoproxy;

import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author <a href="https://www.yanghaoyu.xyz">Howie Young</a><i>on 2021/9/18 22:23<i/>
 * @version 1.0
 */


public class CglibMethodInvocation extends ReflectiveMethodInvocation {

    private final MethodProxy methodProxy;

    public CglibMethodInvocation(Object target, Method method, Object[] arguments, MethodProxy methodProxy) {
        super(target, method, arguments);
        this.methodProxy = methodProxy;
    }

    @Override
    public Object proceed() throws Throwable {
        return this.methodProxy.invoke(this.target, this.arguments);
    }
}
