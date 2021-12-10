package xyz.yanghaoyu.flora.test.testConfiguration.config;

import xyz.yanghaoyu.flora.annotation.Bean;
import xyz.yanghaoyu.flora.annotation.Configuration;
import xyz.yanghaoyu.flora.annotation.Enable;
import xyz.yanghaoyu.flora.annotation.Inject;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/12/6 14:20<i/>
 * @version 1.0
 */

@Configuration
@Enable.Aop
@Enable.PropertyPlaceholder(location = "classpath:token.properties")
@Enable.ComponentScan(basePackages = "xyz.yanghaoyu.flora.test.testConfiguration")
public class MyConfig {
    @Inject.ByName
    Wife wife;

    @Bean
    public Husband husband() {
        return new Husband(wife);
    }
}
