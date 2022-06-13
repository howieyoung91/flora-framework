package xyz.yanghaoyu.flora.core.context.event;

import xyz.yanghaoyu.flora.core.context.ApplicationContext;

public class ApplicationContextEvent extends ApplicationEvent {
    public ApplicationContextEvent(Object source) {
        super(source);
    }

    public final ApplicationContext getApplicationContext() {
        return ((ApplicationContext) getSource());
    }
}
