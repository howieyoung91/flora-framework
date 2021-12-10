package xyz.yanghaoyu.flora.test.testConfiguration.config;

import xyz.yanghaoyu.flora.annotation.Value;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/12/5 17:31<i/>
 * @version 1.0
 */
// @Component
public class Husband {
    // @Inject.ByName
    Wife wife;

    @Value("${username}")
    private String username;

    public Husband() {}

    public Husband(Wife wife) {
        this.wife = wife;
    }

    public Wife getWife() {
        return wife;
    }
}
