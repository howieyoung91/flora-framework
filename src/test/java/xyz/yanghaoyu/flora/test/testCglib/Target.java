package xyz.yanghaoyu.flora.test.testCglib;

public class Target {
    public void a() {
        System.out.println(" a 方法");
    }

    public void b() {
        a();
        System.out.println(" b 方法");
    }
}