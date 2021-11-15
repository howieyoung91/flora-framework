package xyz.yanghaoyu.flora.test.test3.bean;

import xyz.yanghaoyu.flora.annotation.Aop;
import xyz.yanghaoyu.flora.annotation.Component;
import xyz.yanghaoyu.flora.core.aop.interceptor.AdviceChain;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/11/13 14:29<i/>
 * @version 1.0
 */

@Component
@org.aspectj.lang.annotation.Aspect
public class Aspect {

    // BeanFactory beanFactory;

    // @Inject.ByName
    // User user;
    //
    // public User getUser() {
    //     return user;
    // }

    @Aop.Enhance(
            pointcut = "execution(public * xyz.yanghaoyu.flora.test.test3.bean.User.sleep(..))",
            priority = -1
    )
    public Object en4(AdviceChain chain) throws Throwable {
        System.out.println("user sleep before [Aspect]");
        Object res = chain.proceed();
        System.out.println("user sleep after [Aspect]");
        return null;
    }

}
