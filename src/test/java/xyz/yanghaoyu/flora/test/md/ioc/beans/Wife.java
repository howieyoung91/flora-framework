package xyz.yanghaoyu.flora.test.md.ioc.beans;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/12/6 22:17<i/>
 * @version 1.0
 */


public class Wife {
    String name;
    Husband husband;

    public String getName() {
        return name;
    }

    public Wife setName(String name) {
        this.name = name;
        return this;
    }

    public Husband getHusband() {
        return husband;
    }

    public Wife setHusband(Husband husband) {
        this.husband = husband;
        return this;
    }
}
