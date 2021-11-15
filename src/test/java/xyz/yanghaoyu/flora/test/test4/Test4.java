package xyz.yanghaoyu.flora.test.test4;

import org.junit.Test;
import xyz.yanghaoyu.flora.core.context.support.ClassPathXmlApplicationContext;
import xyz.yanghaoyu.flora.test.test4.bean.UserDao;
import xyz.yanghaoyu.flora.test.test4.bean.UserService;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/11/14 21:26<i/>
 * @version 1.0
 */

public class Test4 {
    @Test
    public void t() {
        ClassPathXmlApplicationContext ac =
                new ClassPathXmlApplicationContext("classpath:application-test4.xml");
        UserService us = ac.getBean("userServiceImpl", UserService.class);
        UserDao ud = ac.getBean("userDaoImpl", UserDao.class);
        us.list();
    }

}
