package xyz.yanghaoyu.flora.test;

import org.junit.Test;
import xyz.yanghaoyu.flora.context.support.ClassPathXmlApplicationContext;
import xyz.yanghaoyu.flora.test.bean.UserController;
import xyz.yanghaoyu.flora.test.bean.UserService;

/**
 * @author <a href="https://www.yanghaoyu.xyz">Howie Young</a><i>on 2021/8/7 23:07<i/>
 * @version 1.0
 */

public class TestFlora {
    @Test
    public void test() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:application.xml");
        applicationContext.registerShutdownHook();
        UserController userController = applicationContext.getBean("userController", UserController.class);
        UserService userService = applicationContext.getBean("userServiceImpl", UserService.class);
        userService.say();
        userController.show();
    }
}
