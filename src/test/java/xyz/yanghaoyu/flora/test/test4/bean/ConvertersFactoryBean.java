package xyz.yanghaoyu.flora.test.test4.bean;

import xyz.yanghaoyu.flora.annotation.Component;
import xyz.yanghaoyu.flora.core.beans.factory.FactoryBean;

import java.util.HashSet;
import java.util.Set;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/11/21 20:19<i/>
 * @version 1.0
 */

@Component("converters")
public class ConvertersFactoryBean implements FactoryBean<Set<?>> {

    @Override
    public Set<?> getObject() throws Exception {
        HashSet<Object> converters = new HashSet<>();
        StringToLocalDateConverter stringToLocalDateConverter = new StringToLocalDateConverter("yyyy-MM-dd");
        converters.add(stringToLocalDateConverter);
        return converters;
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
