package xyz.yanghaoyu.flora.core.context.support;

import xyz.yanghaoyu.flora.exception.BeansException;

/**
 * @author <a href="https://www.yanghaoyu.xyz">Howie Young</a><i>on 2021/8/9 11:39<i/>
 * @version 1.0
 */

public class ClassPathXmlApplicationContext extends AbstractXmlApplicationContext {
    /**
     * 从 XML 中加载 BeanDefinition，并刷新上下文
     *
     * @param configLocations
     * @throws BeansException
     */
    public ClassPathXmlApplicationContext(String configLocations) throws BeansException {
        this(new String[]{configLocations});
    }

    /**
     * 从 XML 中加载 BeanDefinition，并刷新上下文
     *
     * @param configLocations
     * @throws BeansException
     */
    public ClassPathXmlApplicationContext(String[] configLocations) throws BeansException {
        super(configLocations);
        refresh();
    }
}
