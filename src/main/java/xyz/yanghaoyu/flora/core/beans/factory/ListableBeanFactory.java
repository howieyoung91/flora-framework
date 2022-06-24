package xyz.yanghaoyu.flora.core.beans.factory;

import xyz.yanghaoyu.flora.exception.BeansException;

import java.util.Map;


/**
 * Extension of the {@link BeanFactory} interface to be implemented by bean factories
 * that can enumerate all their bean instances, rather than attempting bean lookup
 * by name one by one as requested by clients. BeanFactory implementations that
 * preload all their bean definitions (such as XML-based factories) may implement
 * this interface.
 */
public interface ListableBeanFactory extends BeanFactory {
    /**
     * 按照类型返回 Bean 实例
     */
    <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException;

    <T> T getBeanOfType(Class<T> type) throws BeansException;

    /**
     * 返回注册表中所有的Bean名称
     */
    String[] getBeanDefinitionNames();

    <T> String[] getBeanNamesForType(Class<T> type);

    boolean containsBeanDefinition(String beanName);

    int getBeanDefinitionCount();
}
