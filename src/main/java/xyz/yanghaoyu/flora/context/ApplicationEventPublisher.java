package xyz.yanghaoyu.flora.context;

import xyz.yanghaoyu.flora.context.event.ApplicationEvent;

public interface ApplicationEventPublisher {
    void publishEvent(ApplicationEvent event);
}
