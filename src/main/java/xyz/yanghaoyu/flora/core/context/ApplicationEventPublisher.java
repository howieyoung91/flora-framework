package xyz.yanghaoyu.flora.core.context;

import xyz.yanghaoyu.flora.core.context.event.ApplicationEvent;

public interface ApplicationEventPublisher {
    void publishEvent(ApplicationEvent event);
}
