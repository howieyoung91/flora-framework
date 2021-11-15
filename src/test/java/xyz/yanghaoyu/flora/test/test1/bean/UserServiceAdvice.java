package xyz.yanghaoyu.flora.test.test1.bean;

import xyz.yanghaoyu.flora.annotation.Component;
import xyz.yanghaoyu.flora.core.aop.advice.MethodAroundAdvice;

import java.lang.reflect.Method;

/**
 * @author <a href="https://www.yanghaoyu.xyz">Howie Young</a><i>on 2021/8/14 22:33<i/>
 * @version 1.0
 */

@Component
public class UserServiceAdvice implements MethodAroundAdvice {
    @Override
    public Object around(Method method, Object[] args, Object target) throws Throwable {
        System.out.println("UserServiceAdvice before");
        method.invoke(target, args);
        System.out.println("UserServiceAdvice after");
        return null;
    }

}
