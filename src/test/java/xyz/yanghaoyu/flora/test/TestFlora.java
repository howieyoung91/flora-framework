package xyz.yanghaoyu.flora.test;

import org.junit.Test;
import xyz.yanghaoyu.flora.beans.factory.support.DefaultListableBeanFactory;
import xyz.yanghaoyu.flora.context.support.ClassPathXmlApplicationContext;
import xyz.yanghaoyu.flora.core.io.reader.DefaultBeanDefinitionAnnotationReader;

/**
 * @author <a href="https://www.yanghaoyu.xyz">Howie Young</a><i>on 2021/8/7 23:07<i/>
 * @version 1.0
 */

public class TestFlora {
    @Test
    public void test() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:application.xml");
        applicationContext.registerShutdownHook();
        UserService userService = applicationContext.getBean("userService", UserService.class);
        UserDao userDao = applicationContext.getBean("userDao", UserDao.class);
        userService.say();
        userService.t();
        userService.doIUserDao();
    }

    @Test
    public void testAnnoReader() {
        // 1.初始化 BeanFactory
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        DefaultBeanDefinitionAnnotationReader reader = new DefaultBeanDefinitionAnnotationReader(beanFactory);
        reader.loadBeanDefinitions("xyz.yanghaoyu.flora.test");
        UserService userService = (UserService) beanFactory.getBean("userService");
        userService.t();
        userService.say();
        userService.doIUserDao();
    }
}
