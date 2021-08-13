package xyz.yanghaoyu.flora.context;

import xyz.yanghaoyu.flora.context.event.ApplicationEvent;

import java.util.EventListener;

public interface ApplicationListener<E extends ApplicationEvent> extends EventListener {
    void onApplicationEvent(E event);
}
