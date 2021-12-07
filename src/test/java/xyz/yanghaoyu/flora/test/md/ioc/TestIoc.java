package xyz.yanghaoyu.flora.test.md.ioc;

import org.junit.Test;
import xyz.yanghaoyu.flora.core.context.support.AnnotationConfigApplicationContext;
import xyz.yanghaoyu.flora.core.context.support.ClassPathXmlApplicationContext;
import xyz.yanghaoyu.flora.test.md.ioc.beans.Husband;
import xyz.yanghaoyu.flora.test.md.ioc.beans.Wife;
import xyz.yanghaoyu.flora.test.md.ioc.config.Config1;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/12/6 22:16<i/>
 * @version 1.0
 */


public class TestIoc {
    @Test
    public void testIoc() {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:md/ioc.xml");
        Wife wife = ctx.getBean("wife", Wife.class);
        Husband husband = ctx.getBean("husband", Husband.class);
        System.out.println(wife.getHusband() == husband);
        System.out.println(husband.getWife() == wife);
    }

    @Test
    public void testIoc1() {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(Config1.class);
        ctx.registerShutdownHook();
        Husband bean = ctx.getBean("husband", Husband.class);
        Wife wife = ctx.getBean("wife", Wife.class);
        System.out.println(bean);
    }
}
