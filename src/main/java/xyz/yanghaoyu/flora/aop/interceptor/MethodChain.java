package xyz.yanghaoyu.flora.aop.interceptor;

import org.aopalliance.intercept.MethodInvocation;
import xyz.yanghaoyu.flora.structure.Chain;

import java.util.Collection;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/11/12 23:30<i/>
 * @version 1.0
 */


public class MethodChain extends Chain {
    private MethodInvocation methodInvocation;

    public MethodChain(Collection<Point> points) {
        super(points);
    }

    public MethodInvocation getMethodInvocation() {
        return methodInvocation;
    }

    public MethodChain setMethodInvocation(MethodInvocation methodInvocation) {
        this.methodInvocation = methodInvocation;
        return this;
    }

    @Override
    protected Object doEnd() throws Throwable {
        return methodInvocation.getMethod().invoke(methodInvocation.getThis(), methodInvocation.getArguments());
    }
}
