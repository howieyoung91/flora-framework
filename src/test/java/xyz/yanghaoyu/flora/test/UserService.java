package xyz.yanghaoyu.flora.test;

import xyz.yanghaoyu.flora.annotation.Component;
import xyz.yanghaoyu.flora.annotation.Inject;

/**
 * @author <a href="https://www.yanghaoyu.xyz">Howie Young</a><i>on 2021/8/7 23:06<i/>
 * @version 1.0
 */

@Component
public class UserService {
    String name = "Howie";
    @Inject.ByName
    UserDao userDao;

    public UserService() { }

    public UserService(String name) {
        this.name = name;
    }

    public void say() {
        System.out.println("Hello " + name);
    }

    public void t() {
        System.out.println(userDao.temp);
    }
}
