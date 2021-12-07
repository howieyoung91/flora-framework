# IOC 的相关使用

### 1. xml configuration

```java
package xyz.yanghaoyu.demo

public class Wife {
    private String name;
    private Husband husband;
    // getter, setter ...
}

public class Husband {
    private String name;
    private Wife wife;
}
```

在 xml 中声明 bean

```xml
<!-- ioc.xml -->
<beans>
  <bean id="wife" class="xyz.yanghaoyu.demo.Wife">
    <property name="name" value="wife's name"/>
    <property name="husband" ref="husband"/>
  </bean>
  <bean id="husband" class="xyz.yanghaoyu.demo.Husband">
    <property name="name" value="husband's name"/>
    <property name="wife" ref="wife"/>
  </bean>
</beans>

```

```java
public class TestIoc {
    @Test
    public void testIoc() {
        ClassPathXmlApplicationContext ctx
                = new ClassPathXmlApplicationContext("classpath:ioc.xml");
        // 从 context 中获取生成的 bean
        Wife wife = ctx.getBean("wife", Wife.class);
        Husband husband = ctx.getBean("husband", Husband.class);
        System.out.println(wife.getHusband() == husband); // true
        System.out.println(husband.getWife() == wife);    // true
    }
}
```

1. 使用 `<bean />` 声明一个 bean, `id` 应该是唯一的
2. 使用`<property />` 对属性赋值, `value` 代表值对象, `ref` 表示获取容器中对应的 bean 的引用
3. 循环依赖是允许的

#### 2. 组件扫描

在 xml 配置文件中 写入 `<component-scan base-package="${your package}"/>` 开启 组件扫描

```xml

<beans>
  <!-- ... -->
  <component-scan base-package="xyz.yanghaoyu.demo"/>
  <!-- ... -->
</beans>
```

```java
package xyz.yanghaoyu.demo

import xyz.yanghaoyu.flora.annotation.Inject;

@Component
public class Wife {
    private String name;
    @Inject.ByName
    private Husband husband;
    // getter, setter ...
}

@Component
public class Husband {
    private String name;
    @Inject.ByType
    private Wife wife;
    // getter, setter ...
}
```

使用`<component-scan base-package="${your package}"/>` 开启组件扫描器  
组件扫描器能把被`@Component`标记的类自动注册到容器中
`@Inject.ByName`,`@Inject.ByType`能注入 bean

但是此时 name 还没有被注入, 此时需要使用`<enable-property-placeholder location="your properties file"/>`开启`PropertyPlaceholder`

```properties
# token.properties
husbandName=howieyoung
```

```xml
<!-- ioc.xml -->
<beans>
  <!-- ... -->
  <enable-property-placeholder location="classpath:token.properties"/>
  <!-- ... -->
</beans>
```

```java

@Component
public class Husband {
    // it will be injected !!
    @Value("${husbandName}")
    String name;
}
```

此时 框架就能从 `token.properties` 中拿到属性并注入 bean

### 2. java configuration

```java

@Configuration
public class MyConfig {
    @Bean
    public Husband husband() {
        return new Husband(wife());
    }

    @Bean
    public Wife wife() {
        return new Wife();
    }
}

```

```java
public class TestIoc {
    @Test
    public void testIoc() {
        AnnotationConfigApplicationContext ctx
                = new AnnotationConfigApplicationContext(MyConfig.class);
        Husband husband = ctx.getBean("husband", Husband.class);
        System.out.println(husband.getWife());
    }
}
```

1. 使用 `@Configuration` 声明配置类
2. 使用`@Bean`注册 Bean
3. 默认开启组件扫描

这两个 Bean 都是单例的 在 `MyConfig#husband` 中调用 `MyConfig#wife` 本质上会从容器中取出 `wife` 对象 并不会生成多个 `wife`, 除了直接调用方法, 还可以使用参数获得 bean,
如以下代码:

```java

@Configuration
public class Config1 {
    @Bean
    public Husband husband(Wife wife) {
        Husband husband = new Husband();
        // ok
        husband.setWife(wife);
        return husband;
    }
}

@Configuration
public class Config2 {
    @Bean
    public Wife wife() {
        return new Wife();
    }
}
```

这里有 2 个配置类,它们并没有什么关联, 你有两种方式使它们关联起来

1.给出多个启动配置类
`new AnnotationConfigApplicationContext(Config1.class, Config2.class);`使用多个配置类进行配置

```java
public class TestIoc {
    @Test
    public void testIoc() {
        AnnotationConfigApplicationContext ctx =
                new AnnotationConfigApplicationContext(Config1.class, Config2.class);
        Husband husband = ctx.getBean("husband", Husband.class);
        System.out.println(husband.getWife());
    }
}
```

2. 使用`@Import.Configuration` 来导入其他配置类

```java

@Configuration
@Import.Configuration(configClasses = Config2.class)
public class Config1 {
    @Bean
    public Husband husband(Wife wife) {
        Husband husband = new Husband();
        // ok
        husband.setWife(wife);
        return husband;
    }
}

@Configuration
// 循环导入是允许的,并不会产生无限递归
// @Import.Configuration(configClasses = Config1.class)
public class Config2 {
    @Bean
    public Wife wife() {
        return new Wife();
    }
}

public class TestIoc {
    @Test
    public void testIoc() {
        AnnotationConfigApplicationContext ctx =
                // 给出一个即可
                new AnnotationConfigApplicationContext(Config1.class);
        // 都给出也是可以的
        // new AnnotationConfigApplicationContext(Config1.class, Config2.class);
        Husband husband = ctx.getBean("husband", Husband.class);
        System.out.println(husband.getWife());
    }
}
```

### 单例和多例

1. 基于 xml configuration

```xml

<bean id="husband" class="xyz.yanghaoyu.demo.Husband" scope="prototype">
  // ...
</bean>
```

在<bean/> 写入 `scope="prototype"` 即可声明为多例  
同理`scope="singleton"` 可以声明为单例, 当然, 单例是默认的

2. 基于 java config

`@Scope.Prototype` 表示这是多例的, 那么每次从容器中获取该对象时都会创建一个新的 bean
`@Scope.Singleton` 单例模式,这是默认的, 每次从容器中获取的对象时都是同一个对象

```java

@Component
@Scope.Prototype
public class Husband {
    private String name;
    @Inject.ByType
    private Wife wife;
    // getter, setter ...
}
```

#### 初始化方法和销毁方法

初始化方法允许你在属性被注入之后执行一段自己的逻辑 销毁方法允许你在对象将被销毁之前执行一段自己的逻辑

1.实现 `InitializingBean`接口和`DisposableBean`接口来实现初始化方法和销毁方法

```java

@Component
public class Husband implements InitializingBean {
    private String name;
    @Inject.ByType
    private Wife wife;
    // ...

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("this is init method");
    }

    //...
}
```

2. 基于 xml configuration

- 在<bean/> 写入 `init-method="${initMethod}"` 可以指定 初始化方法
- 在<bean/> 写入 `destroy-method="${destroyMethod}"` 可以指定 销毁方法

```xml

<bean id="husband" class="xyz.yanghaoyu.demo.Husband"
      init-method="${initMethod}"
      destroy-method="${destroyMethod}"
>
  // ...
</bean>
```

3. 基于 java config

在对应方法上使用 `@Life.Initialize` 和`@Life.Destroy`即可指定初始化方法和销毁方法

注解和`InitializingBean`,`DisposableBean` 不能同时使用

```java

@Component
public class Husband {
    private String name;
    @Inject.ByType
    private Wife wife;

    @Life.Initialize
    public void init() throws Exception {
        System.out.println("this is init method");
    }

    @Life.Destroy
    public void destroy() throws Exception {
        System.out.println("this is destroy method");
    }
}
```
