package xyz.yanghaoyu.flora.test.test4.bean;

import xyz.yanghaoyu.flora.annotation.Aop;
import xyz.yanghaoyu.flora.annotation.Component;
import xyz.yanghaoyu.flora.aop.interceptor.AdviceChain;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/11/13 14:29<i/>
 * @version 1.0
 */

@Component
@org.aspectj.lang.annotation.Aspect
public class Aspect {
    @Aop.Enhance(
            pointcut = "execution(* xyz.yanghaoyu.flora.test.test4.bean.UserService.list(..))",
            priority = -1
    )
    public Object en4(AdviceChain chain) throws Throwable {
        System.out.println("before");
        Object res = chain.proceed();
        System.out.println("after");
        return res;
    }
}
