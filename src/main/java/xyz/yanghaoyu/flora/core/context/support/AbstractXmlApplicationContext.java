package xyz.yanghaoyu.flora.core.context.support;

import xyz.yanghaoyu.flora.core.beans.factory.support.DefaultListableBeanFactory;
import xyz.yanghaoyu.flora.core.beans.factory.xml.XmlBeanDefinitionReader;

/**
 * @author <a href="https://www.yanghaoyu.xyz">Howie Young</a><i>on 2021/8/9 11:38<i/>
 * @version 1.0
 */


public abstract class AbstractXmlApplicationContext extends AbstractRefreshableApplicationContext {
    @Override
    protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) {
        XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory, this);
        String[] configLocations = getConfigLocations();
        if (null != configLocations) {
            beanDefinitionReader.loadBeanDefinitions(configLocations);
        }
    }

    protected abstract String[] getConfigLocations();

}
