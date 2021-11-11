package xyz.yanghaoyu.flora.test.event;

import xyz.yanghaoyu.flora.context.ApplicationListener;
import xyz.yanghaoyu.flora.context.event.ContextRefreshedEvent;

public class Listener implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        System.out.println("refresh 完毕!");
    }
}
