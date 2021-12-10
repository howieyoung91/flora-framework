package xyz.yanghaoyu.flora.test.testByteBuddy;

import net.bytebuddy.asm.Advice;

import java.lang.reflect.Method;

class LoggerAdvisor {
    @Advice.OnMethodEnter
    public static void onMethodEnter(
            @Advice.Origin Method method,
            @Advice.AllArguments Object[] arguments
    ) {
        System.out.println("enter");
    }

    @Advice.OnMethodExit
    public static void onMethodExit(
            @Advice.Origin Method
                    method,
            @Advice.AllArguments
                    Object[] arguments,
            @Advice.Return
                    Object ret
    ) {
        System.out.println("exit");
    }
}