package xyz.yanghaoyu.flora.test.test5;

import xyz.yanghaoyu.flora.core.context.support.ClassPathXmlApplicationContext;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/11/22 13:51<i/>
 * @version 1.0
 */


public class Test5 {
    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext ac =
                new ClassPathXmlApplicationContext("classpath:application-test5.xml");
        MyFactoryBean myFactoryBean = ac.getBean("&myFactoryBean", MyFactoryBean.class);
        System.out.println(myFactoryBean.getObject());
    }
}
