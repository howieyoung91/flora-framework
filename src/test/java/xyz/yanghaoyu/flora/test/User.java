package xyz.yanghaoyu.flora.test;

/**
 * @author <a href="https://www.yanghaoyu.xyz">Howie Young</a><i>on 2021/8/7 23:06<i/>
 * @version 1.0
 */


public class User {
    String name = "Howie";
    UserDao userDao;

    public User() { }

    public User(String name) {
        this.name = name;
    }

    public void say() {
        System.out.println("Hello " + name);
    }

    public void t() {
        System.out.println(userDao.temp);
    }
}
