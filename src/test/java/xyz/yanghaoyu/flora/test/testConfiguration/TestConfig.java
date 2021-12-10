package xyz.yanghaoyu.flora.test.testConfiguration;

import org.junit.Test;
import xyz.yanghaoyu.flora.core.context.support.ClassPathXmlApplicationContext;
import xyz.yanghaoyu.flora.test.testConfiguration.config.Husband;
import xyz.yanghaoyu.flora.test.testConfiguration.config.Wife;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/12/5 17:07<i/>
 * @version 1.0
 */


public class TestConfig {
    @Test
    public void testconfig() {
        ClassPathXmlApplicationContext ac =
                new ClassPathXmlApplicationContext("classpath:application-testConfiguration.xml");
        // ac.registerShutdownHook();
        Husband bean = ac.getBean("husband", Husband.class);
        Wife wife = ac.getBean("wife", Wife.class);
        bean.getWife();
    }
}
