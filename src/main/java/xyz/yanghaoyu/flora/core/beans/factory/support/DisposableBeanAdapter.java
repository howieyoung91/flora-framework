package xyz.yanghaoyu.flora.core.beans.factory.support;

import xyz.yanghaoyu.flora.core.beans.factory.config.BeanDefinition;
import xyz.yanghaoyu.flora.util.StringUtil;

import java.lang.reflect.Method;

/**
 * 销毁方法有两种甚至多种方式, 需要一个适配器,给JVM提供一个统一的方法
 *
 * @author <a href="https://www.yanghaoyu.xyz">Howie Young</a><i>on 2021/8/10 16:58<i/>
 * @version 1.0
 */

public class DisposableBeanAdapter implements DisposableBean {
    private final Object bean;
    private final String beanName;
    private String destroyMethodName;

    public DisposableBeanAdapter(Object bean, String beanName, BeanDefinition beanDefinition) {
        this.bean = bean;
        this.beanName = beanName;
        this.destroyMethodName = beanDefinition.getDestroyMethodName();
    }

    @Override
    public void destroy() throws Exception {
        // 1. 实现接口 DisposableBean
        if (bean instanceof DisposableBean) {
            ((DisposableBean) bean).destroy();
        }

        // 2. 配置信息 destroy-method {判断是为了避免二次执行销毁}
        if (StringUtil.isEmpty(destroyMethodName) || (bean instanceof DisposableBean && "destroy".equals(this.destroyMethodName))) {
            return;
        }
        Method destroyMethod = bean.getClass().getMethod(destroyMethodName);
        destroyMethod.invoke(bean);
    }
}
