package xyz.yanghaoyu.flora.test.testCglib;

public class Target {
    String id;

    public void a() {
        System.out.println("a 方法");
    }

    public void b() {
        System.out.println(id);
        a();
        System.out.println("b 方法");
    }
}