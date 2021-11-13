package xyz.yanghaoyu.flora.test.test3.bean;

import xyz.yanghaoyu.flora.annotation.Aop;
import xyz.yanghaoyu.flora.annotation.Component;
import xyz.yanghaoyu.flora.aop.interceptor.MethodChain;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/11/13 14:29<i/>
 * @version 1.0
 */

@Component
public class UserAspect {
    @Aop.Enhance(
            value = "execution(* xyz.yanghaoyu.flora.test.test3.bean.User.*(..))",
            priority = 0
    )
    public Object en2(MethodChain chain) throws Throwable {
        System.out.println("user say before  [UserAspect]");
        Object res = chain.proceed();
        System.out.println("user say after   [UserAspect]");
        return res;
    }

    @Aop.Enhance(
            value = "execution(* xyz.yanghaoyu.flora.test.test3.bean.User.say(..))",
            priority = 2
    )
    public Object en(MethodChain chain) throws Throwable {
        System.out.println("user say before  [UserAspect]");
        Object res = chain.proceed();
        System.out.println("user say after   [UserAspect]");
        return res;
    }

    @Aop.Enhance(
            value = "execution(* xyz.yanghaoyu.flora.test.test3.bean.User.say(..))",
            priority = 1
    )
    public Object en1(MethodChain chain) throws Throwable {
        System.out.println("user say before1   [UserAspect]");
        Object res = chain.proceed();
        System.out.println("user say after1   [UserAspect]");
        return res;
    }
}
