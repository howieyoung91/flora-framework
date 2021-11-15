package xyz.yanghaoyu.flora.test.test1;

import xyz.yanghaoyu.flora.core.context.support.ClassPathXmlApplicationContext;
import xyz.yanghaoyu.flora.test.test1.bean.UserService;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/11/11 18:34<i/>
 * @version 1.0
 */


public class Test {
    @org.junit.Test
    public void test() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:application.xml");
        applicationContext.registerShutdownHook();
        UserService userService = applicationContext.getBean("userServiceImpl", UserService.class);
        userService.say();
        userService.doSth();
    }
}
