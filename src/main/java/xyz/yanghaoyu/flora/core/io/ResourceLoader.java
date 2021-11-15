package xyz.yanghaoyu.flora.core.io;

import xyz.yanghaoyu.flora.core.io.Resource;

/**
 * @author <a href="https://www.yanghaoyu.xyz">Howie Young</a><i>on 2021/8/8 10:52<i/>
 * @version 1.0
 */


public interface ResourceLoader {
    String CLASSPATH_URL_PREFIX = "classpath:";

    Resource getResource(String location);
}
