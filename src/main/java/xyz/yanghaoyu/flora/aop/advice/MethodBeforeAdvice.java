package xyz.yanghaoyu.flora.aop.advice;

import java.lang.reflect.Method;

/**
 * 不支持 Before
 *
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/8/12 18:02<i/>
 * @version 1.0
 */

@Deprecated
public interface MethodBeforeAdvice extends BeforeAdvice {
    void before(Method method, Object[] args, Object target) throws Throwable;
}
