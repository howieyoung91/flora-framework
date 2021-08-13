package xyz.yanghaoyu.flora.beans.factory;

import xyz.yanghaoyu.flora.BeansException;
import xyz.yanghaoyu.flora.context.ApplicationContext;


public interface ApplicationContextAware extends Aware {
    void setApplicationContext(ApplicationContext applicationContext) throws BeansException;
}
