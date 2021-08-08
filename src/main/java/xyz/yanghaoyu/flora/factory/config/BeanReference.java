package xyz.yanghaoyu.flora.factory.config;

/**
 * @author <a href="https://www.yanghaoyu.xyz">Howie Young</a><i>on 2021/8/7 23:54<i/>
 * @version 1.0
 */


public class BeanReference {
    String beanName;

    public BeanReference(String beanName) {
        this.beanName = beanName;
    }

    public String getBeanName() {
        return beanName;
    }
}
