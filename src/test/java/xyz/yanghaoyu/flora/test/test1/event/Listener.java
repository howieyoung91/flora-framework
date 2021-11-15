package xyz.yanghaoyu.flora.test.test1.event;

import xyz.yanghaoyu.flora.core.context.ApplicationListener;
import xyz.yanghaoyu.flora.core.context.event.ContextRefreshedEvent;

public class Listener implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        System.out.println("refresh 完毕!");
    }
}
