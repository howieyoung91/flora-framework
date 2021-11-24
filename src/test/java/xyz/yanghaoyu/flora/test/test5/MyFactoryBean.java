package xyz.yanghaoyu.flora.test.test5;

import xyz.yanghaoyu.flora.annotation.Component;
import xyz.yanghaoyu.flora.core.beans.factory.FactoryBean;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/11/22 13:51<i/>
 * @version 1.0
 */

@Component
public class MyFactoryBean implements FactoryBean<String> {

    @Override
    public String getObject() throws Exception {
        return "howieyoung";
    }

    @Override
    public Class<?> getObjectType() {
        return String.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
