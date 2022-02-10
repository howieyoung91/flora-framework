package xyz.yanghaoyu.flora.core.beans.factory.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.yanghaoyu.flora.annotation.Configuration;
import xyz.yanghaoyu.flora.core.beans.factory.ConfigurableListableBeanFactory;
import xyz.yanghaoyu.flora.core.beans.factory.support.DefaultListableBeanFactory;
import xyz.yanghaoyu.flora.exception.BeansException;

import java.util.HashSet;
import java.util.Set;

/**
 * 对 @Configuration @Bean 做支持
 *
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/12/4 22:57<i/>
 * @version 1.0
 */

public class ConfigurationBeanBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationBeanBeanFactoryPostProcessor.class);

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configBeanFactory) throws BeansException {
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) configBeanFactory;
        LOGGER.trace("resolve [Configuration] ...");
        HashSet<String> classes = findConfigBeanName(beanFactory);

        LOGGER.trace("scan [Configuration] from {} ...", classes);
        ConfigurationBeanClassScanner configurationClassParser =
                new ConfigurationBeanClassScanner(beanFactory, classes);
        Set<String> configBeanNames = configurationClassParser.scan();
        LOGGER.trace("find [Configuration] -> {}", configBeanNames);

        ConfigurationBeanClassParser parser =
                new ConfigurationBeanClassParser(beanFactory, configBeanNames);
        parser.parse();

        LOGGER.trace("finish resolve [Configuration]");
    }

    private HashSet<String> findConfigBeanName(DefaultListableBeanFactory beanFactory) {
        String[] names = beanFactory.getBeanDefinitionNames();
        HashSet<String> classes = new HashSet<>();
        for (String name : names) {
            BeanDefinition configBeanDef = beanFactory.getBeanDefinition(name);
            Class<?> configBeanClass = configBeanDef.getBeanClass();
            if (!configBeanClass.isAnnotationPresent(Configuration.class)) {
                continue;
            }
            classes.add(name);
        }
        return classes;
    }
}
