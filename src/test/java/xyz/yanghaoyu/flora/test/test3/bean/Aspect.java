package xyz.yanghaoyu.flora.test.test3.bean;

import xyz.yanghaoyu.flora.annotation.Aop;
import xyz.yanghaoyu.flora.annotation.Component;
import xyz.yanghaoyu.flora.aop.interceptor.MethodChain;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/11/13 14:29<i/>
 * @version 1.0
 */

@Component
public class Aspect {

    @Aop.Enhance(
            value = "execution(* xyz.yanghaoyu.flora.test.test3.bean.User.say(..))"
    )
    public Object en(MethodChain chain) throws Throwable {
        System.out.println("user say before [Aspect]");
        Object res = chain.proceed();
        System.out.println("user say after [Aspect]");
        return res;
    }

    @Aop.Enhance(
            value = "execution(* xyz.yanghaoyu.flora.test.test3.bean.User.say(..))"
    )
    public Object en1(MethodChain chain) throws Throwable {
        System.out.println("user say before1 [Aspect]");
        Object res = chain.proceed();
        System.out.println("user say after1 [Aspect]");
        return res;
    }
}
