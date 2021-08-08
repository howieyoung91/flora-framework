package xyz.yanghaoyu.flora.core.context;

import xyz.yanghaoyu.flora.core.context.event.ApplicationEvent;

import java.util.EventListener;

public interface ApplicationListener<E extends ApplicationEvent> extends EventListener {
    void onApplicationEvent(E event);
}
