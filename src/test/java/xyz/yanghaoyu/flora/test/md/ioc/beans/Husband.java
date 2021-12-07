package xyz.yanghaoyu.flora.test.md.ioc.beans;

import xyz.yanghaoyu.flora.annotation.Life;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/12/6 22:17<i/>
 * @version 1.0
 */


public class Husband {
    String name;
    Wife wife;

    public String getName() {
        return name;
    }

    public Husband setName(String name) {
        this.name = name;
        return this;
    }

    public Wife getWife() {
        return wife;
    }

    public Husband setWife(Wife wife) {
        this.wife = wife;
        return this;
    }

    @Life.Initialize
    public void afterPropertiesSet() throws Exception {
        System.out.println("this is init method");
    }

    @Life.Destroy
    public void destroy() throws Exception {
        System.out.println("this is destroy method");
    }
}
