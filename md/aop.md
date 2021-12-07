# AOP 的相关使用

### 开启 AOP

使用 AOP 需要手动开启相关功能

可以使用以下两种方式开启 AOP

```xml

<enable-aop/>
```

```java

@Configuration
@Enable.Aop
public class Config {
    //...
}
```

### 使用 AOP

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