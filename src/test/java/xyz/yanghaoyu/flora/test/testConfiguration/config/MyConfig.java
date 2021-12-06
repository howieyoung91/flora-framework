package xyz.yanghaoyu.flora.test.testConfiguration.config;

import xyz.yanghaoyu.flora.annotation.Bean;
import xyz.yanghaoyu.flora.annotation.Configuration;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/12/6 14:20<i/>
 * @version 1.0
 */

@Configuration
public class MyConfig {
    @Bean
    public HusbandAspect husbandAspect() {
        return new HusbandAspect();
    }

    // @Bean
    // public Husband husband() {
    //     return new Husband(wife());
    // }

    @Bean
    public Wife wife() {
        return new Wife();
    }
}
