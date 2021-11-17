# Flora

## Feature

### 1. IOC

#### component-scan

在配置文件中 写入 `<component-scan base-package="${your package}"/>` 开启 组件扫描

```xml

<beans>
  <!-- ... -->
  <component-scan base-package="xyz.yanghaoyu.bean"/>
  <!-- ... -->
</beans>
```

```java
package xyz.yanghaoyu.bean

@Component
// 可以配置为单例或者多例
// @Scope.Singleton
// @Scope.Prototype
public class Bean {

}
```

#### PropertyPlaceholder

在配置文件中 写入 `<enable-property-placeholder location="your properties file"/>` 开启

```java

@Component
public class User {
    // it will be injected !!
    @Value("${username}")
    String username;
}
```

token.properties

```properties
username=howieyoung
```

application.xml

```xml

<beans>
  <!-- ... -->
  <enable-property-placeholder location="classpath:token.properties"/>
  <!-- ... -->
</beans>
```

#### 循环依赖

```java

@Component
public class Wife {
    @Inject.ByName
    Husband husband;
}
```

```java

@Component
public class Husband {
    @Inject.ByName
    Wife wife;
}
```

this is correct. it works without errors ~~

### 2. AOP

Spring 中 `@Around` 对应的注解为 `@Aop.Enhance`

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

在配置文件中 写入 `<enable-aop/>` 开启 AOP

```java
package xyz.yanghaoyu.demo

@Component
public class User {
    public void say() {
        System.out.println("user say");
    }
}
```

```java

@Component
@Aspect
public class UserAspect {
    @Aop.Enhance(
            pointcut = "execution(* xyz.yanghaoyu.demo.User.*(..))",
            priority = -1000
    )
    public Object e(AdviceChain chain) throws Throwable {
        System.out.println("user All before ");
        Object res = chain.proceed();
        System.out.println("user All after ");
        return res;
    }

}

```

TODO

- [ ] Java Class Config
