package xyz.yanghaoyu.flora.aop.autoproxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import xyz.yanghaoyu.flora.aop.AdvisedSupport;

import java.lang.reflect.Method;

/**
 * @author <a href="https://www.yanghaoyu.xyz">Howie Young</a><i>on 2021/8/12 18:10<i/>
 * @version 1.0
 */


public class Cglib2AopProxy implements AopProxy {
    private final AdvisedSupport advised;

    public Cglib2AopProxy(AdvisedSupport advised) {
        this.advised = advised;
    }

    @Override
    public Object getProxy() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(advised.getTargetSource().getTarget().getClass());
        enhancer.setInterfaces(advised.getTargetSource().getTargetClass());
        enhancer.setCallback(new DynamicAdvisedInterceptor(advised));
        return enhancer.create();
    }

    private static class DynamicAdvisedInterceptor implements MethodInterceptor {

        private final AdvisedSupport advised;

        public DynamicAdvisedInterceptor(AdvisedSupport advised) {
            this.advised = advised;
        }

        @Override
        public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
            // 方法匹配
            if (advised.getMethodMatcher().matches(
                    method, advised.getTargetSource().getTarget().getClass())
            ) {
                // Chain

                // 使用客户端的增强
                return advised.getMethodInterceptor().invoke(
                        new CglibMethodInvocation(
                                advised.getTargetSource().getTarget(),
                                method, args, methodProxy
                        ));
            }
            // 不使用增强,直接调用真实对象的方法
            return methodProxy.invoke(advised.getTargetSource().getTarget(), args);
        }
    }
}
