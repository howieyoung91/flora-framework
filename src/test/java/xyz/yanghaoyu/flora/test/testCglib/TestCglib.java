package xyz.yanghaoyu.flora.test.testCglib;

import net.sf.cglib.beans.BeanGenerator;
import net.sf.cglib.beans.BeanMap;
import net.sf.cglib.proxy.CallbackFilter;
import net.sf.cglib.proxy.Enhancer;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/12/5 13:55<i/>
 * @version 1.0
 */


public class TestCglib {
    private static class Filter implements CallbackFilter {
        @Override
        public int accept(Method method) {
            return 0;
        }
    }

    @Test
    public void t() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        Enhancer e = new Enhancer();

        //
        // e.setSuperclass(Target.class);
        // e.setCallbackFilter(new Filter());
        // Class[] callbackTypes = {MyMethodInterceptor.class};
        // e.setCallbackTypes(callbackTypes);
        // // e.
        // Class subclass = e.createClass();
        // Enhancer.registerStaticCallbacks(subclass, new MyMethodInterceptor[]{new MyMethodInterceptor()});
        //
        //


        BeanGenerator beanGenerator = new BeanGenerator();
        beanGenerator.setSuperclass(Target.class);
        beanGenerator.addProperty("id", String.class);

        Class<?> aClass = (Class) beanGenerator.createClass();
        // beanGenerator
        System.out.println(Enhancer.isEnhanced(aClass));
        Enhancer.registerStaticCallbacks(aClass, new MyMethodInterceptor[]{new MyMethodInterceptor()});

        Object o1 = aClass.newInstance();
        // beanGenerator.

        Target o11 = (Target) o1;
        o11.b();

        // Object o = subclass.newInstance();

        // Target target = (Target) o;
        // target.b();

        for (Field declaredField : aClass.getDeclaredFields()) {
            declaredField.setAccessible(true);
            System.out.println(declaredField.getName());
        }

        // e.setCallback(new MyMethodInterceptor());
    }

    @Test
    public void test() throws IllegalAccessException {
        BeanGenerator beanGenerator = new BeanGenerator();

        // beanGenerator.addProperty("id", String.class);

        beanGenerator.setSuperclass(User.class);
        for (Field field : User.class.getDeclaredFields()) {
            field.setAccessible(true);
            beanGenerator.addProperty(field.getName(), field.getType());
        }
        Object o = beanGenerator.create();
        BeanMap beanMap = BeanMap.create(o);

        for (Field field : User.class.getDeclaredFields()) {
            field.setAccessible(true);
            beanMap.put(field.getName(), "username");
        }
        Field[] fields = o.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            System.out.println(field.getName() + " --- " + field.get(o));
        }
        User user = (User) o;
        user.test();
        // System.out.println(fields.length);
    }
}
