package xyz.yanghaoyu.flora.test.test3;

import org.junit.Test;
import xyz.yanghaoyu.flora.context.support.ClassPathXmlApplicationContext;
import xyz.yanghaoyu.flora.test.test3.bean.Aspect;
import xyz.yanghaoyu.flora.test.test3.bean.User;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/11/13 14:28<i/>
 * @version 1.0
 */


public class Test3 {

    @Test
    public void test() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:application-test3.xml");
        applicationContext.registerShutdownHook();
        User user = applicationContext.getBean("user", User.class);
        Aspect aspect = applicationContext.getBean("aspect", Aspect.class);
        user.sleep();
        user.say();
        // aspect.getUser().sleep();
        // Object user = applicationContext.getBean("user");
        // System.out.println(user.getClass().getSimpleName());
        // user.say();
        // user.sleep();
    }
}
