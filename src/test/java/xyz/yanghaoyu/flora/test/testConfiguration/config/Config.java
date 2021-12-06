package xyz.yanghaoyu.flora.test.testConfiguration.config;

import xyz.yanghaoyu.flora.annotation.Bean;
import xyz.yanghaoyu.flora.annotation.Configuration;
import xyz.yanghaoyu.flora.annotation.Enable;
import xyz.yanghaoyu.flora.annotation.Inject;
import xyz.yanghaoyu.flora.test.testConfiguration.bean.Husband;
import xyz.yanghaoyu.flora.test.testConfiguration.bean.Wife;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/12/5 17:07<i/>
 * @version 1.0
 */

@Configuration
@Enable.ComponentScan(basePackages = "xyz.yanghaoyu.flora.test.testConfiguration.bean")
public class Config {
    @Bean
    public Wife wife() {
        return new Wife();
    }

    @Bean
    public Husband husband(
            @Inject.ByName("wife")
                    Wife wife,
            @Inject.ByName("wife")
                    Wife wife1
    ) {
        return new Husband(wife);
    }

    @Bean
    public Husband husband1(Wife wife) {
        return new Husband(wife);
    }
}
