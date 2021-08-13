package xyz.yanghaoyu.flora.context.event;

public class ApplicationContextEvent extends ApplicationEvent {
    public ApplicationContextEvent(Object source) {
        super(source);
    }

    public ApplicationEvent getApplicationContext() {
        return ((ApplicationEvent) getSource());
    }
}
