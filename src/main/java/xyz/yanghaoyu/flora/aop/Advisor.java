package xyz.yanghaoyu.flora.aop;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;

import java.util.List;

/**
 * @author <a href="https://www.yanghaoyu.xyz">Howie Young</a><i>on 2021/8/12 17:53<i/>
 * @version 1.0
 */

public interface Advisor {
    Advice getAdvice();

    // List<MethodInterceptor> getAdvices();
}
