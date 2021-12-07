package xyz.yanghaoyu.flora.test.md.ioc.config;

import xyz.yanghaoyu.flora.annotation.Bean;
import xyz.yanghaoyu.flora.annotation.Configuration;
import xyz.yanghaoyu.flora.annotation.Import;
import xyz.yanghaoyu.flora.test.md.ioc.beans.Wife;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/12/7 10:25<i/>
 * @version 1.0
 */

@Configuration
@Import.Configuration(configClasses = Config1.class)
public class Config2 {
    @Bean
    public Wife wife() {
        return new Wife();
    }
}
