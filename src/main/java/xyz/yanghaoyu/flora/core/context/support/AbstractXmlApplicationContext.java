package xyz.yanghaoyu.flora.core.context.support;

import xyz.yanghaoyu.flora.core.beans.factory.support.DefaultListableBeanFactory;
import xyz.yanghaoyu.flora.core.io.reader.XmlBeanDefinitionReader;
import xyz.yanghaoyu.flora.exception.BeansException;

/**
 * @author <a href="https://www.yanghaoyu.xyz">Howie Young</a><i>on 2021/8/9 11:38<i/>
 * @version 1.0
 */


public abstract class AbstractXmlApplicationContext extends AbstractRefreshableConfigurableFileApplicationContext {
    public AbstractXmlApplicationContext(String configLocations) throws BeansException {
        setConfigLocations(configLocations);
    }

    public AbstractXmlApplicationContext(String[] configLocations) throws BeansException {
        setConfigLocations(configLocations);
    }

    @Override
    protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) {
        XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory, this);
        String[] configLocations = getConfigLocations();
        if (null != configLocations) {
            beanDefinitionReader.loadBeanDefinitions(configLocations);
        }
    }
}
