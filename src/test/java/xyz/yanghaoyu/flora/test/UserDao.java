package xyz.yanghaoyu.flora.test;

import xyz.yanghaoyu.flora.annotation.Component;
import xyz.yanghaoyu.flora.beans.factory.support.DisposableBean;
import xyz.yanghaoyu.flora.beans.factory.support.InitializingBean;

/**
 * @author <a href="https://www.yanghaoyu.xyz">Howie Young</a><i>on 2021/8/8 00:10<i/>
 * @version 1.0
 */

@Component
public class UserDao implements InitializingBean, DisposableBean {
    String temp = "123";

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("userDao: InitializingBean");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("userDao: DisposableBean");
    }


    public void init() {
        System.out.println("userDao: XML init");
    }

    public void close() {
        System.out.println("userDao: XML destroy");
    }
}
