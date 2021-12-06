package xyz.yanghaoyu.flora.test.testConfiguration.bean;

import xyz.yanghaoyu.flora.annotation.Bean;
import xyz.yanghaoyu.flora.annotation.Configuration;
import xyz.yanghaoyu.flora.annotation.Inject;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/12/5 17:07<i/>
 * @version 1.0
 */

@Configuration
public class Config {
    @Bean
    public Husband husband(
            Wife wife,
            @Inject.ByName("husband1")
                    Husband husband1
    ) {
        return new Husband(wife);
    }

    @Bean
    public Husband husband1(Wife wife) {
        return new Husband(wife);
    }
}
