package xyz.yanghaoyu.flora.test.test02;

import xyz.yanghaoyu.flora.context.support.ClassPathXmlApplicationContext;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/11/11 18:32<i/>
 * @version 1.0
 */


public class Test {
    /**
     * 测试循环依赖
     */

    @org.junit.Test
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
