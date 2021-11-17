package xyz.yanghaoyu.flora.core.aop.autoproxy.annotation;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import xyz.yanghaoyu.flora.core.aop.AnnotationAdvisedSupport;
import xyz.yanghaoyu.flora.core.aop.MethodMatcher;
import xyz.yanghaoyu.flora.core.aop.autoproxy.CglibMethodInvocation;
import xyz.yanghaoyu.flora.core.aop.interceptor.MultiMethodInterceptor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Annotation cglib 自动代理
 *
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/11/14 <i/>
 * @version 1.0
 */

public class AnnotationCglib2AopProxy implements MultiAopProxy, MethodInterceptor {
    private final AnnotationAdvisedSupport advisedSupport;

    private final Map<Method, MultiMethodInterceptor> cache = new HashMap<>();

    public AnnotationCglib2AopProxy(AnnotationAdvisedSupport advised) {
        this.advisedSupport = advised;
    }

    @Override
    public Object getProxy() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(advisedSupport.getTargetSource().getTarget().getClass());
        enhancer.setInterfaces(advisedSupport.getTargetSource().getTargetClass());
        enhancer.setCallback(this);
        return enhancer.create();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        MultiMethodInterceptor interceptor = cache.get(method);
        if (interceptor != null) {
            // cache hit happened
            return invokeProxyMethod(method, args, methodProxy, interceptor);
        }

        // 获取真实的拦截器 主要目的是把多个拦截器合并为一个 确保优先级正确 (Red-Black Tree)
        // 这里不应该循环触发拦截器 因为会造成方法的多次调用
        interceptor = getRealMethodInterceptor(method);

        if (interceptor == null) {
            return invokeRealMethod(args, methodProxy);
        } else {
            cache.put(method, interceptor);
            return invokeProxyMethod(method, args, methodProxy, interceptor);
        }
    }

    private MultiMethodInterceptor getRealMethodInterceptor(Method method) {
        /*
         * 如果有多个拦截器拦截到同一个方法 要合并一下拦截器
         *
         * 例如:
         *  ① xyz.yanghaoyu.User.*(..)   point1 -> realMethod
         *  ② xyz.yanghaoyu.User.say(..) point2 -> realMethod
         *  都会拦截到 User#say
         *  需要把 ① ② 的拦截器合并成为一个拦截器 point1 -> point2 -> realMethod
         */
        return merge(findMatchedMethodInterceptor(method));
    }

    private ArrayList<MultiMethodInterceptor> findMatchedMethodInterceptor(Method method) {
        /*
         * 从拦截该类的拦截器中找到拦截器当前方法的拦截器 并加入 ArrayList
         */
        Set<Map.Entry<MethodMatcher, MultiMethodInterceptor>> set = advisedSupport.getMethodInterceptorSet();
        ArrayList<MultiMethodInterceptor> temp = new ArrayList<>();
        for (Map.Entry<MethodMatcher, MultiMethodInterceptor> entry : set) {
            MethodMatcher methodMatcher = entry.getKey();
            MultiMethodInterceptor multiMethodInterceptor = entry.getValue();
            if (methodMatcher.matches(method, advisedSupport.getTargetSource().getTarget().getClass())) {
                temp.add(multiMethodInterceptor);
            }
        }
        return temp;
    }

    private Object invokeProxyMethod(Method method, Object[] args, MethodProxy methodProxy, MultiMethodInterceptor interceptor) throws Throwable {
        return interceptor.invoke(
                new CglibMethodInvocation(
                        advisedSupport.getTargetSource().getTarget(),
                        method, args, methodProxy
                ));
    }

    private Object invokeRealMethod(Object[] args, MethodProxy methodProxy) throws Throwable {
        // return method.invoke(advisedSupport.getTargetSource().getTarget(), args)
        return methodProxy.invoke(advisedSupport.getTargetSource().getTarget(), args);
    }
}
