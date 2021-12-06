package xyz.yanghaoyu.flora.test.testConfiguration;

import org.junit.Test;
import xyz.yanghaoyu.flora.core.beans.factory.config.ConfigurationClassParser;
import xyz.yanghaoyu.flora.core.context.support.ClassPathXmlApplicationContext;
import xyz.yanghaoyu.flora.test.testConfiguration.bean.Husband;
import xyz.yanghaoyu.flora.test.testConfiguration.config.Config;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/12/5 17:07<i/>
 * @version 1.0
 */


public class TestConfig {
    @Test
    public void testconfig() {
        ClassPathXmlApplicationContext ac =
                new ClassPathXmlApplicationContext("classpath:application-testConfiguration.xml");
        Husband bean = ac.getBean("husband", Husband.class);
        System.out.println(bean.getWife());
        // Husband husband1 = ac.getBean("husband1", Husband.class);
        // System.out.println(husband1.getWife());
        // System.out.println(bean.getWife());
    }

    @Test
    public void t() {
    }
}
