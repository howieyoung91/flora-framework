# AOP 的相关使用

### 开启 AOP

使用 AOP 需要手动开启相关功能

可以使用以下两种方式开启 AOP

1. xml configuration

```xml

<enable-aop/>
```

2. java configuration

```java

@Configuration
@Enable.Aop
public class Config {
    //...
}
```

### 使用 AOP

```java
package xyz.yanghaoyu.demo

@Component
public class User {
    public void say() {
        System.out.println("user say");
    }
}
```

使用 `@Aop.Enhance` 对方法 User#say 进行增强

```java

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@interface Enhance {
    // 要增强的方法
    String pointcut();

    // 优先级 越小先执行
    int priority();
}
```

编写切面类 `UserAspect`

```java

@Component
@Aspect
public class UserAspect {
    @Aop.Enhance(
            // 切点表达式
            pointcut = "execution(* xyz.yanghaoyu.demo.User.*(..))",
            priority = -1000
    )
    public Object e(AdviceChain chain) throws Throwable {
        // 前置增强
        System.out.println("User#say before");
        // 让拦截器链继续执行
        Object res = chain.proceed();
        // 后置增强
        System.out.println("User#say after");
        return res;
    }

}

```

此时, 从容器中取出 `user` 再调用`say` 将会输出

```
User#say before
user say
User#say after
```

AOP 仅支持 注解实现, XML 已经无法实现 AOP 了, 这一段已经写死在代码中,若要 XML 也支持 AOP 需要修改一下 `IocUtil`

```java
public class IocUtil {
    // ...
    public static void enableAop(BeanDefinitionRegistry registry) {
        // 把这段代码取消注释即可开启 XML的AOP
        // registry.registerBeanDefinition(
        //         DefaultAdvisorAutoProxyCreator.class.getName(),
        //         new BeanDefinition(DefaultAdvisorAutoProxyCreator.class)
        // );

        // 注解 AOP
        registry.registerBeanDefinition(
                AnnotationAwareAspectJAutoProxySupportBeanFactoryPostProcessor.class.getName(),
                new BeanDefinition(AnnotationAwareAspectJAutoProxySupportBeanFactoryPostProcessor.class)
        );
        registry.registerBeanDefinition(
                AnnotationAwareAspectJAutoProxyCreator.class.getName(),
                new BeanDefinition(AnnotationAwareAspectJAutoProxyCreator.class)
        );
    }
    // ...
}
```

注意: 如果在切面类中使用@Inject 注入的对象将不会被代理!


