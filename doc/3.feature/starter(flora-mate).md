### starter

使用 `starter (flora-mate)` 可以引入一些配置使得外部的框架更快地与 flora 集成在一起，这得益于 flora 内部的 SPI机制

#### example

写一个 starter 非常简单，只需要在你的maven项目的 resource/META-INF/flora.yaml 列出自动配置类即可

```yaml
flora:
  framework:
    autoconfiguration:
      - xyz.yanghaoyu.flora.test.MyAutoConfiguration
```

自动配置类写在 `flora.framework.autoconfiguration`里

```java

@Configuration
public class MyAutoConfiguration {
    // ...
}
```

在 flora 启动过程中这个文件将会被自动读入，这些自动配置类将会被自动地注入容器