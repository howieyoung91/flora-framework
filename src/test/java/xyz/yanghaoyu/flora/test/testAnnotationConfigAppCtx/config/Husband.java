package xyz.yanghaoyu.flora.test.testAnnotationConfigAppCtx.config;

import xyz.yanghaoyu.flora.annotation.Life;
import xyz.yanghaoyu.flora.annotation.Value;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/12/5 17:31<i/>
 * @version 1.0
 */
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
        System.out.println(username);
        return wife;
    }

    @Life.Initialize
    @Life.Destroy
    public void afterPropertiesSet() throws Exception {
        System.out.println("afterPropertiesSet");
    }
}
