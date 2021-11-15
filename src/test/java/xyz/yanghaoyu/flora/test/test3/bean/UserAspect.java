package xyz.yanghaoyu.flora.test.test3.bean;

import org.aspectj.lang.annotation.Aspect;
import xyz.yanghaoyu.flora.annotation.Aop;
import xyz.yanghaoyu.flora.annotation.Component;
import xyz.yanghaoyu.flora.core.aop.interceptor.AdviceChain;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/11/13 14:29<i/>
 * @version 1.0
 */

@Component
@Aspect
public class UserAspect {
    @Aop.Enhance(
            pointcut = "execution(* xyz.yanghaoyu.flora.test.test3.bean.User.*(..))",
            priority = -1000
    )
    public Object en2(AdviceChain chain) throws Throwable {
        System.out.println("user All before  [UserAspect]");
        Object res = chain.proceed();
        System.out.println("user All after   [UserAspect]");
        return res;
    }


    @Aop.Enhance(
            pointcut = "execution(* xyz.yanghaoyu.flora.test.test3.bean.User.sleep(..))",
            priority = 2
    )
    public Object en(AdviceChain chain) throws Throwable {
        System.out.println("user say before  [UserAspect]");
        Object res = chain.proceed();
        System.out.println("user say after   [UserAspect]");
        return res;
    }
}
