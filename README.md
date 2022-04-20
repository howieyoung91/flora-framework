# flora-framework

## install

使用
`git clone git@github.com:howieyoung91/flora-framework.git`克隆代码

cd 到项目目录

执行
`mvn install` 安装到本地仓库

## Feature

- IOC
    - XML 配置  
      `<bean id="bean" class="..." .../>`
    - component-scan  
      `@Enable.ComponentScan`  
      `<component-scan base-package="${your package}"`

    - java class 配置
      ```java
      @Configuration
      public class Config {
        @Bean
        public Bean bean(){
          return new Bean();
        }
      }
      ```
    - 属性占位符替换`PropertyPlaceholder`&`PropertySource`
    - 初始化方法 和 销毁方法
    - 单例 `Bean` 和多例 `Bean`

* AOP
    - 关于切面编程 我只实现了 `Spring` 中的 `@Around`, 并且不支持`xml`配置切面(已经实现, 但被弃用)
* 其他
    - 三级缓存解决循环依赖
    - 类型转换 `Converter`
    - 自动配置属性 `@ConfigurationProperties`
    - 对象感知功能 `Aware`
    - 容器事件 `AppicationEnvent`
    - 等等

### 已经实现注解:

1. `@Component`
2. `@Scope.Singleton` <=>`@Scope("singleton")`
3. `@Scope.Prototype`<=> `@Scope("prototype")`
4. `@Inject.ByType` <=>`@Autowired`
5. `@Inject.ByName` <=>`@Resource`
6. `@Life.Initialize` <=>`@PostConstruct`
7. `@Life.Destory`<=> `@PreDestory`
8. `@Value`
10. `@Order`
11. `@Aop.Enhance`<=> `@Around`
12. `@Configuration`
13. `@Bean`
14. `@ConfigurationProperties`
15. `@Enable.ComponentScan` <=>`@EnableComponentScan`
16. `@Enable.PropertySource` <=>`@PropertySource`
17. `@Enable.Aop`<=>`@EnableAspectJAutoProxy`
18. `@Import.Configuration` <=>`@Import`
19. `@Import.Resource`<=>`@ImportResource`

目前支持的注解：

## TODO

- [ ] Log Config

### DOC:

- [x] [IOC](/doc/1.ioc)
    - [x] [xml](/doc/1.ioc/1.xml.md)
    - [x] [@Configuration & @Bean](/doc/1.ioc/2.@Configuration.md)
    - [x] [ComponentScan](/doc/1.ioc/3.ComponentScan.md)
    - [x] [PropertyPlaceholder](/doc/1.ioc/4.PropertyPlaceholder.md)
    - [x] [Initialize Method & Destroy Method](/doc/1.ioc/5.Initializing&Destroy.md)
    - [x] [Singleton & Prototype](/doc/1.ioc/6.Singleton&Prototype.md)
- [x] [AOP](/doc/2.aop)
    - [x] [环绕增强](/doc/2.aop/aop.md)
- [x] MORE
    - [x] [TypeConverter](/doc/3.feature/converter.md)