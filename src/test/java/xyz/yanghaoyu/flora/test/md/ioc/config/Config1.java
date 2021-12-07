package xyz.yanghaoyu.flora.test.md.ioc.config;

import xyz.yanghaoyu.flora.annotation.Bean;
import xyz.yanghaoyu.flora.annotation.Configuration;
import xyz.yanghaoyu.flora.annotation.Import;
import xyz.yanghaoyu.flora.test.md.ioc.beans.Husband;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/12/7 10:25<i/>
 * @version 1.0
 */

@Configuration
@Import.Configuration(configClasses = Config2.class)
public class Config1 {
    @Bean
    public Husband husband() {
        return new Husband();
    }
}
