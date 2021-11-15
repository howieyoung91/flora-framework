package xyz.yanghaoyu.flora.test.test2;

import xyz.yanghaoyu.flora.annotation.Aop;
import xyz.yanghaoyu.flora.core.aop.interceptor.AdviceChain;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/11/12 15:39<i/>
 * @version 1.0
 */

// @Component
public class HusbandAdvice {
    @Aop.Enhance(pointcut = "execution(* xyz.yanghaoyu.flora.test.test2.Husband.*(..))", priority = 0)
    public Object getWife(AdviceChain chain) throws Throwable {
        return chain.proceed();
    }
}
