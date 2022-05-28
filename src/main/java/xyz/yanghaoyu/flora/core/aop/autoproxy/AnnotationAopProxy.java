package xyz.yanghaoyu.flora.core.aop.autoproxy;

import xyz.yanghaoyu.flora.core.aop.AnnotationAdvisedSupport;
import xyz.yanghaoyu.flora.core.aop.MethodMatcher;
import xyz.yanghaoyu.flora.core.aop.interceptor.MultiMethodInterceptor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2022/2/10 18:12<i/>
 * @version 1.0
 */

public abstract class AnnotationAopProxy implements MultiOrderedAopProxy {
    protected final AnnotationAdvisedSupport            advisedSupport;
    protected final Map<Method, MultiMethodInterceptor> cache = new ConcurrentHashMap<>();

    public AnnotationAopProxy(AnnotationAdvisedSupport advisedSupport) {
        this.advisedSupport = advisedSupport;
    }

    protected ArrayList<MultiMethodInterceptor> findMatchedMethodInterceptor(Method method) {
        ArrayList<MultiMethodInterceptor> interceptors = new ArrayList<>();
        for (Map.Entry<MethodMatcher, MultiMethodInterceptor> entry : advisedSupport.getMethodInterceptorSet()) {
            MethodMatcher          matcher     = entry.getKey();
            MultiMethodInterceptor interceptor = entry.getValue();

            Class<?> targetClass = advisedSupport.getTargetSource().getTarget().getClass();
            if (matcher.matches(method, targetClass)) {
                interceptors.add(interceptor);
            }
        }
        return interceptors;
    }

    /**
     * 如果有多个拦截器拦截到同一个方法 要合并一下拦截器
     * <p>
     * 例如:
     * ① xyz.yanghaoyu.User.*(..)   point1 -> realMethod
     * ② xyz.yanghaoyu.User.say(..) point2 -> realMethod
     * 都会拦截到 User#say
     * 需要把 ① ② 的拦截器合并成为一个拦截器 point1 -> point2 -> realMethod
     */
    protected MultiMethodInterceptor getEnhanceMethodInterceptor(Method method) {
        return mergeAndSort(findMatchedMethodInterceptor(method));
    }
}
