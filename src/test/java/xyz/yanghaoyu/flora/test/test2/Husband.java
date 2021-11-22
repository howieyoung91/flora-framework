package xyz.yanghaoyu.flora.test.test2;

import xyz.yanghaoyu.flora.annotation.Component;
import xyz.yanghaoyu.flora.annotation.Inject;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/11/12 15:39<i/>
 * @version 1.0
 */

@Component
public class Husband {
    @Inject.ByName
    private Wife wife;

    public Wife getWife() {
        return wife;
    }

    public Husband setWife(Wife wife) {
        this.wife = wife;
        return this;
    }
}