package xyz.yanghaoyu.flora.test.test1.event;

import xyz.yanghaoyu.flora.annotation.Component;
import xyz.yanghaoyu.flora.core.context.ApplicationListener;
import xyz.yanghaoyu.flora.core.context.event.ContextRefreshedEvent;

// @Component
public class Listener implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        System.out.println("refresh 完毕!");
    }
}
