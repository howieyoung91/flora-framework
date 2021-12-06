package xyz.yanghaoyu.flora.test.testAnnotationConfigAppCtx.config;

import xyz.yanghaoyu.flora.annotation.*;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/12/6 14:20<i/>
 * @version 1.0
 */

@Configuration
@Enable.Aop
@Import.Configuration(configClasses = Config.class)
@Enable.PropertyPlaceholder(location = "classpath:token.properties")
public class MyConfig {
    @Bean
    public HusbandAspect husbandAspect() {
        return new HusbandAspect();
    }

    @Bean
    public Husband husband(Wife wife, @Inject.ByType Wife wife1) {
        System.out.println(wife);
        System.out.println(wife1);
        return new Husband(wife);
    }
}
