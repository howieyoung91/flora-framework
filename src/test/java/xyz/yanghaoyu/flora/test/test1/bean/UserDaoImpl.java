package xyz.yanghaoyu.flora.test.test1.bean;

import xyz.yanghaoyu.flora.annotation.Component;
import xyz.yanghaoyu.flora.core.beans.factory.BeanNameAware;
import xyz.yanghaoyu.flora.core.beans.factory.support.DisposableBean;
import xyz.yanghaoyu.flora.core.beans.factory.support.InitializingBean;
import xyz.yanghaoyu.flora.exception.BeansException;

/**
 * @author <a href="https://www.yanghaoyu.xyz">Howie Young</a><i>on 2021/8/14 13:58<i/>
 * @version 1.0
 */

@Component
public class UserDaoImpl implements UserDao, InitializingBean, DisposableBean, BeanNameAware {
    String beanName;

    @Override
    public void destroy() throws Exception {
        System.out.println(beanName + " destroy");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println(beanName + " init");
    }

    @Override
    public void setBeanName(String beanName) throws BeansException {
        this.beanName = beanName;
    }
}
