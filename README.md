# Flora

## How to use

使用
`git clone git@github.com:howieyoung91/flora-framework.git`克隆代码

cd 到项目目录

执行
`mvn install` 安装到本地仓库

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

AOP 仅支持 Annotation 实现, XML 已经无法实现 AOP 了, 这一段已经写死在代码中,若要XML也支持 AOP 需要修改一下 `IocUtil`

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

目前切面类还不支持 自动注入

## TODO

- [ ] Java Class Config
- [ ] Log Config
