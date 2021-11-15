package xyz.yanghaoyu.flora.test.test2;

import org.junit.Test;
import xyz.yanghaoyu.flora.context.support.ClassPathXmlApplicationContext;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/11/12 15:40<i/>
 * @version 1.0
 */

public class Test2 {
    @Test
    public void test() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:application-test2.xml");
        applicationContext.registerShutdownHook();
        Husband husband = applicationContext.getBean("husband", Husband.class);
        Wife wife = applicationContext.getBean("wife", Wife.class);
        Wife wife1 = applicationContext.getBean("wife", Wife.class);
        // Wife wife1 = husband.getWife();
        System.out.println(wife1 == wife);
        System.out.println(wife1.getHusband() == wife.getHusband());
    }
}
