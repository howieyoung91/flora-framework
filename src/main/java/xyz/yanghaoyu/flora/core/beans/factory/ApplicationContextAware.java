package xyz.yanghaoyu.flora.core.beans.factory;

import xyz.yanghaoyu.flora.exception.BeansException;
import xyz.yanghaoyu.flora.core.context.ApplicationContext;


public interface ApplicationContextAware extends Aware {
    void setApplicationContext(ApplicationContext applicationContext) throws BeansException;
}
