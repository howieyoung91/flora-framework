package xyz.yanghaoyu.flora.test;

import org.junit.Test;
import xyz.yanghaoyu.flora.context.support.ClassPathXmlApplicationContext;
import xyz.yanghaoyu.flora.test.bean.UserService;
import xyz.yanghaoyu.flora.test.test02.Husband;
import xyz.yanghaoyu.flora.test.test02.Three;
import xyz.yanghaoyu.flora.test.test02.Wife;

/**
 * @author <a href="https://www.yanghaoyu.xyz">Howie Young</a><i>on 2021/8/7 23:07<i/>
 * @version 1.0
 */

public class TestFlora {
    @Test
    public void test() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:application.xml");
        applicationContext.registerShutdownHook();
        UserService userService = applicationContext.getBean("userServiceImpl", UserService.class);
        userService.say();
        userService.doSth();
    }

    /**
     * 测试循环依赖
     */
    @Test
    public void test01() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:application.xml");
        applicationContext.registerShutdownHook();
        Wife wife = applicationContext.getBean("wife", Wife.class);
        Husband husband = applicationContext.getBean("husband", Husband.class);
        Three three = applicationContext.getBean("three", Three.class);
        System.out.println(husband.getWife() == wife);
        System.out.println(wife.getThree() == three);
        System.out.println(three.getHusband() == husband);
    }
}
