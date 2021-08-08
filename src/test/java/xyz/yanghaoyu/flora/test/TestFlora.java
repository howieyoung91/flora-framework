package xyz.yanghaoyu.flora.test;

import org.junit.Test;
import xyz.yanghaoyu.flora.beans.factory.support.DefaultListableBeanFactory;
import xyz.yanghaoyu.flora.core.io.reader.XmlBeanDefinitionReader;

/**
 * @author <a href="https://www.yanghaoyu.xyz">Howie Young</a><i>on 2021/8/7 23:07<i/>
 * @version 1.0
 */

public class TestFlora {
    @Test
    public void test() {
        // 1.初始化 BeanFactory
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        // 2. 读取配置文件&注册Bean
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        reader.loadBeanDefinitions("classpath:application.xml");
        // 3. 获取Bean对象调用方法
        User userService = (User) beanFactory.getBean("userService");
        userService.t();
        userService.say();
    }
}
