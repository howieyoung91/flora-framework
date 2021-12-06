package xyz.yanghaoyu.flora.test.testConfiguration.config;

import xyz.yanghaoyu.flora.annotation.Component;
import xyz.yanghaoyu.flora.annotation.Inject;
import xyz.yanghaoyu.flora.annotation.Life;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/12/5 17:31<i/>
 * @version 1.0
 */
@Component
public class Husband {
    @Inject.ByName
    Wife wife;

    public Husband() {}

    public Husband(Wife wife) {
        this.wife = wife;
    }

    public Wife getWife() {
        return wife;
    }

    @Life.Initialize
    @Life.Destroy
    public void afterPropertiesSet() throws Exception {
        System.out.println("afterPropertiesSet");
    }
}
