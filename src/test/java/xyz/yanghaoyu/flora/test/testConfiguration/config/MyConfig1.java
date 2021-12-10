package xyz.yanghaoyu.flora.test.testConfiguration.config;

import xyz.yanghaoyu.flora.annotation.Bean;
import xyz.yanghaoyu.flora.annotation.Configuration;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/12/10 17:56<i/>
 * @version 1.0
 */

@Configuration
public class MyConfig1 {
    @Bean
    public Wife wife() {
        return new Wife();
    }
}
