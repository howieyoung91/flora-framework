package xyz.yanghaoyu.flora.test.testaop.aspect;

import org.aspectj.lang.annotation.Aspect;
import xyz.yanghaoyu.flora.annotation.Aop;
import xyz.yanghaoyu.flora.annotation.Component;
import xyz.yanghaoyu.flora.core.aop.interceptor.AdviceChain;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/12/6 16:21<i/>
 * @version 1.0
 */

@Component
@Aspect
public class HusbandAspect {
    @Aop.Enhance(
            pointcut = "execution(public * xyz.yanghaoyu.flora.test.testaop.beans.*.*(..))",
            priority = -1
    )
    public Object enhance(AdviceChain chain) throws Throwable {
        System.out.println("advice before");
        Object proceed = chain.proceed();
        System.out.println("advice after");
        return proceed;
    }
}
