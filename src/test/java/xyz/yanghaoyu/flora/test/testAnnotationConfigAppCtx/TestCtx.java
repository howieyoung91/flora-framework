package xyz.yanghaoyu.flora.test.testAnnotationConfigAppCtx;

import org.junit.Test;
import xyz.yanghaoyu.flora.core.context.support.AnnotationConfigApplicationContext;
import xyz.yanghaoyu.flora.test.testAnnotationConfigAppCtx.config.Config;
import xyz.yanghaoyu.flora.test.testAnnotationConfigAppCtx.config.Husband;
import xyz.yanghaoyu.flora.test.testAnnotationConfigAppCtx.config.MyConfig;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/12/6 20:15<i/>
 * @version 1.0
 */


public class TestCtx {
    @Test
    public void t() {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(Config.class, MyConfig.class);
        Husband husband = ctx.getBean("husband", Husband.class);
        System.out.println(husband.getWife());
        // System.out.println(husband);
    }
}
