package xyz.yanghaoyu.flora;

import org.junit.Test;
import xyz.yanghaoyu.flora.factory.PropertyValue;
import xyz.yanghaoyu.flora.factory.PropertyValues;
import xyz.yanghaoyu.flora.factory.config.BeanDefinition;
import xyz.yanghaoyu.flora.factory.config.BeanReference;
import xyz.yanghaoyu.flora.factory.support.DefaultListableBeanFactory;

/**
 * @author <a href="https://www.yanghaoyu.xyz">Howie Young</a><i>on 2021/8/7 23:07<i/>
 * @version 1.0
 */


public class TestFlora {
    @Test
    public void test() {
        DefaultListableBeanFactory bf = new DefaultListableBeanFactory();
        bf.registerBeanDefinition("userDao", new BeanDefinition(UserDao.class, new PropertyValues().addPropertyValue(new PropertyValue("temp", "123"))));
        BeanDefinition beanDefinition = new BeanDefinition(User.class);
        PropertyValue propertyValue = new PropertyValue("name", "howieyoung");
        PropertyValue pv = new PropertyValue("userDao", new BeanReference("userDao"));
        beanDefinition.getPropertyValues().addPropertyValue(propertyValue).addPropertyValue(pv);
        bf.registerBeanDefinition("user", beanDefinition);
        User user = (User) bf.getBean("user", "123");
        user.t();
        user.say();
    }
}
