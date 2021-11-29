package xyz.yanghaoyu.flora.test.test1.bean;

import xyz.yanghaoyu.flora.annotation.Component;
import xyz.yanghaoyu.flora.annotation.Inject;

/**
 * @author <a href="https://www.yanghaoyu.xyz">Howie Young</a><i>on 2021/8/14 13:58<i/>
 * @version 1.0
 */

@Component
public class UserServiceImpl implements UserService {
    @Inject.ByType
    UserDao userDao;
    // @Value(value = "${token}")
    String temp;
    UserController userController;

    @Override
    public void say() {
        System.out.println("userServiceImpl say");
        System.out.println(temp);
    }

    public void doSth() {
        System.out.println("doSth");
    }
}
