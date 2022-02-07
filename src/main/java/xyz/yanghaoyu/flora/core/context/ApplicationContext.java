package xyz.yanghaoyu.flora.core.context;

import xyz.yanghaoyu.flora.core.beans.factory.ListableBeanFactory;
import xyz.yanghaoyu.flora.core.io.ResourceLoader;

/**
 * @author <a href="https://www.yanghaoyu.xyz">Howie Young</a><i>on 2021/8/9 11:37<i/>
 * @version 1.0
 */


public interface ApplicationContext extends ListableBeanFactory, ResourceLoader, ApplicationEventPublisher {
}
