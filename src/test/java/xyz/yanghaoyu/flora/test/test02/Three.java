package xyz.yanghaoyu.flora.test.test02;

import xyz.yanghaoyu.flora.annotation.Component;
import xyz.yanghaoyu.flora.annotation.Inject;

/**
 * @author <a href="https://www.yanghaoyu.xyz">Howie Young</a><i>on 2021/9/17 20:21<i/>
 * @version 1.0
 */

@Component
public class Three {
    @Inject.ByName
    private Husband husband;

    public Husband getHusband() {
        return husband;
    }
}
