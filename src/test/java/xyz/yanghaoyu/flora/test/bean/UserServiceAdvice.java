package xyz.yanghaoyu.flora.test.bean;

import xyz.yanghaoyu.flora.annotation.Component;
import xyz.yanghaoyu.flora.aop.advice.MethodBeforeAdvice;

import java.lang.reflect.Method;

/**
 * @author <a href="https://www.yanghaoyu.xyz">Howie Young</a><i>on 2021/8/14 22:33<i/>
 * @version 1.0
 */

@Component
public class UserServiceAdvice implements MethodBeforeAdvice {
    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        System.out.println("method before");
    }
}
