package xyz.yanghaoyu.flora.core.io.reader;

import xyz.yanghaoyu.flora.core.io.BeanDefinitionReader;
import xyz.yanghaoyu.flora.exception.BeansException;

import java.io.IOException;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/12/6 20:25<i/>
 * @version 1.0
 */


public interface BeanDefinitionAnnotationReader extends BeanDefinitionReader {
    void loadBeanDefinitions(Class... configClasses) throws BeansException, IOException, ClassNotFoundException;
}
