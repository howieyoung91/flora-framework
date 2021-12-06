package xyz.yanghaoyu.flora.test.testAnnotationConfigAppCtx.config;

import xyz.yanghaoyu.flora.annotation.Bean;
import xyz.yanghaoyu.flora.annotation.Configuration;
import xyz.yanghaoyu.flora.annotation.Import;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/12/6 21:24<i/>
 * @version 1.0
 */

@Configuration
@Import.Resource(resources = "classpath:application-testConfiguration.xml")
public class Config {
    @Bean
    public Wife wife() {
        return new Wife();
    }
}
