package xyz.yanghaoyu.flora.core.aop.interceptor;

import org.aopalliance.intercept.MethodInvocation;
import xyz.yanghaoyu.flora.structure.Chain;

import java.util.Collection;
import java.util.TreeSet;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/11/12 23:30<i/>
 * @version 1.0
 */


public class AdviceChain extends Chain {
    private MethodInvocation methodInvocation;

    public AdviceChain() {
        super(new TreeSet<>());
    }

    public AdviceChain(Collection<Point> points) {
        super(points);
    }

    public MethodInvocation getMethodInvocation() {
        return methodInvocation;
    }

    public AdviceChain setMethodInvocation(MethodInvocation methodInvocation) {
        this.methodInvocation = methodInvocation;
        return this;
    }

    @Override
    protected Object doEnd() throws Throwable {
        return methodInvocation.getMethod().invoke(methodInvocation.getThis(), methodInvocation.getArguments());
    }
}
