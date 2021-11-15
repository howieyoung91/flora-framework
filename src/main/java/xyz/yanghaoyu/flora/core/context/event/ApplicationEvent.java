package xyz.yanghaoyu.flora.core.context.event;

import java.util.EventObject;

/**
 * @author <a href="https://www.yanghaoyu.xyz">Howie Young</a><i>on 2021/8/11 20:23<i/>
 * @version 1.0
 */


public abstract class ApplicationEvent extends EventObject {
    public ApplicationEvent(Object source) {
        super(source);
    }
}
