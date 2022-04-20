package xyz.yanghaoyu.flora.core.aop.interceptor;

import org.aopalliance.intercept.MethodInvocation;
import xyz.yanghaoyu.flora.structure.Chain;

import java.util.Collection;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/11/12 23:30<i/>
 * @version 1.0
 */


public class AdviceChain extends Chain {
    private MethodInvocation methodInvocation;

    public AdviceChain(Collection<AdvicePoint> points) {
        super((Collection) points);
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
        // 在执行链的最后调用真实的方法
        return methodInvocation.getMethod().invoke(methodInvocation.getThis(), methodInvocation.getArguments());
    }
}
