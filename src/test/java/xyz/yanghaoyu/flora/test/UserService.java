package xyz.yanghaoyu.flora.test;

import xyz.yanghaoyu.flora.BeansException;
import xyz.yanghaoyu.flora.annotation.Component;
import xyz.yanghaoyu.flora.annotation.Inject;
import xyz.yanghaoyu.flora.beans.factory.ApplicationContextAware;
import xyz.yanghaoyu.flora.beans.factory.BeanFactory;
import xyz.yanghaoyu.flora.beans.factory.BeanFactoryAware;
import xyz.yanghaoyu.flora.context.ApplicationContext;

/**
 * @author <a href="https://www.yanghaoyu.xyz">Howie Young</a><i>on 2021/8/7 23:06<i/>
 * @version 1.0
 */

@Component
public class UserService implements BeanFactoryAware, ApplicationContextAware {
    String name = "Howie";
    @Inject.ByName
    UserDao userDao;
    IUserDao iUserDao;

    public UserService() { }

    public UserService(String name) {
        this.name = name;
    }

    public void say() {
        System.out.println("Hello " + name);
    }

    public void t() {
        System.out.println(userDao.temp);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        System.out.println("BeanFactoryAware: " + beanFactory);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("ApplicationContext: " + applicationContext);
    }

    public void doIUserDao() {
        System.out.println(iUserDao);
        // System.out.println("FactoryBean: " + iUserDao);
    }
}
