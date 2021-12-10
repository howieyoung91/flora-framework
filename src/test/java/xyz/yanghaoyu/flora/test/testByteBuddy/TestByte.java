package xyz.yanghaoyu.flora.test.testByteBuddy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.implementation.MethodDelegation;
import org.junit.Test;
import xyz.yanghaoyu.flora.annotation.Bean;
import xyz.yanghaoyu.flora.annotation.Value;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static net.bytebuddy.matcher.ElementMatchers.any;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/12/9 14:49<i/>
 * @version 1.0
 */


public class TestByte {
    @Test
    public void t() {

        Method[] methods = Bean.class.getMethods();
        for (Method method : methods) {
            // method.setAccessible(true);
            System.out.println(method.getName());
        }

    }

    @Test
    public void test() throws InstantiationException, IllegalAccessException, NoSuchFieldException {

        AnnotationDescription annDesc = AnnotationDescription.Latent.Builder
                .ofType(Value.class).define("value", "")
                .build();

        Foo dynamicFoo = new ByteBuddy()
                .subclass(Foo.class)
                .method(any())
                // .intercept(Advice.to(LoggerAdvisor.class))
                .intercept(MethodDelegation.to(FooEnhancer.class))
                .defineField("id", String.class)
                .annotateField(annDesc)
                .make()
                .load(getClass().getClassLoader())
                .getLoaded()
                .newInstance();
        dynamicFoo.foo();
        Field[] declaredFields = dynamicFoo.getClass().getDeclaredFields();
        // MemberSubstitution.relaxed().field(named("a")).onRead().replaceWith()

        for (Field declaredField : declaredFields) {
            System.out.println(declaredField.getName());
            Annotation[] annotations = declaredField.getAnnotations();
            for (Annotation annotation : annotations) {
                System.out.println(annotation);
            }
        }

        // System.out.println(dynamicFoo.foo());
        // System.out.println(dynamicFoo.husbandAspect());
        // dynamicFoo.husband();
    }
}

