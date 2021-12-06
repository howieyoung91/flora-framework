package xyz.yanghaoyu.flora.test.testCglib;

import net.sf.cglib.proxy.Enhancer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/12/5 13:55<i/>
 * @version 1.0
 */


public class Test {
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        Enhancer e = new Enhancer();
        e.setSuperclass(Target.class);
        // e.setCallbackType(MyMethodInterceptor.class);
        e.setCallback(new MyMethodInterceptor());
        Class aClass = e.createClass();
        Object o = aClass.newInstance();
        Method a = Target.class.getMethod("b");
        a.invoke(o);
    }
}
