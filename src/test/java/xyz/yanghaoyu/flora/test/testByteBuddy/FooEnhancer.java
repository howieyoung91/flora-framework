package xyz.yanghaoyu.flora.test.testByteBuddy;

import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.implementation.bind.annotation.This;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/12/9 15:25<i/>
 * @version 1.0
 */


public class FooEnhancer {
    @RuntimeType
    public static Object foo1(
            @Origin Method method,
            @This Object o,
            @SuperCall Callable<?> callable
    ) throws Exception {
        System.out.println("enhance");
        return callable.call();
        // System.out.println(method.getName());
        // return "enhance";
    }
}
