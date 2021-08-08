package xyz.yanghaoyu.flora.core.aop;

import org.aopalliance.aop.Advice;

/**
 * @author <a href="https://www.yanghaoyu.xyz">Howie Young</a><i>on 2021/8/12 17:53<i/>
 * @version 1.0
 */

public interface Advisor {
    Advice getAdvice();
}
