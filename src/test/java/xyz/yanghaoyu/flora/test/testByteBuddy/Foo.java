package xyz.yanghaoyu.flora.test.testByteBuddy;

import xyz.yanghaoyu.flora.annotation.Bean;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class Foo {
    String id;

    public String bar() {return null;}


    public String foo() {
        System.out.println("actual foo()");
        this.foo(123);
        return null;
    }

    public String foo(Object o) {
        System.out.println("actual foo(Object)");
        return null;
    }

    @Bean
    public String husbandAspect() {
        return "husbandAspect";
    }

    @Bean
    public String husband() {
        return "husband";
    }

    @Bean("wife1")
    public String wife() {
        return "wife";
    }
}