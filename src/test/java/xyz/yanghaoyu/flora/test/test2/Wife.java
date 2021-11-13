package xyz.yanghaoyu.flora.test.test2;

import xyz.yanghaoyu.flora.annotation.Component;
import xyz.yanghaoyu.flora.annotation.Inject;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/11/12 15:38<i/>
 * @version 1.0
 */

@Component
public class Wife {
    @Inject.ByName
    private Husband husband;

    public Husband getHusband() {
        return husband;
    }

    public Wife setHusband(Husband husband) {
        this.husband = husband;
        return this;
    }
}
