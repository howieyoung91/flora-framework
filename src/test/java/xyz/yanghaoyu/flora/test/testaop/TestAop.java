package xyz.yanghaoyu.flora.test.testaop;

import xyz.yanghaoyu.flora.core.context.support.AnnotationConfigApplicationContext;
import xyz.yanghaoyu.flora.test.testaop.beans.Husband;
import xyz.yanghaoyu.flora.test.testaop.beans.Wife;
import xyz.yanghaoyu.flora.test.testaop.config.MyConfig;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/12/8 09:31<i/>
 * @version 1.0
 */


public class TestAop {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext c = new AnnotationConfigApplicationContext(MyConfig.class);
        c.registerShutdownHook();
        Husband husband = c.getBean("husband", Husband.class);
        husband.getWife();
        Wife wife = c.getBean("wife", Wife.class);
        wife.say();
    }
}
