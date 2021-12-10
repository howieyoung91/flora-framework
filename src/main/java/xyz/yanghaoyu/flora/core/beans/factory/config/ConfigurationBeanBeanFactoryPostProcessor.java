package xyz.yanghaoyu.flora.core.beans.factory.config;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.yanghaoyu.flora.annotation.Bean;
import xyz.yanghaoyu.flora.annotation.Configuration;
import xyz.yanghaoyu.flora.core.beans.factory.ConfigurableListableBeanFactory;
import xyz.yanghaoyu.flora.core.beans.factory.support.DefaultListableBeanFactory;
import xyz.yanghaoyu.flora.exception.BeansException;
import xyz.yanghaoyu.flora.util.ComponentUtil;

import java.lang.reflect.Method;
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
    private static DefaultListableBeanFactory beanFactory;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configBeanFactory) throws BeansException {
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) configBeanFactory;

        ConfigurationBeanBeanFactoryPostProcessor.beanFactory = beanFactory;

        String[] names = beanFactory.getBeanDefinitionNames();
        HashSet<String> classes = new HashSet<>();

        for (String name : names) {
            BeanDefinition configBeanDef = beanFactory.getBeanDefinition(name);
            Class<?> configBeanClass = configBeanDef.getBeanClass();
            Configuration configAnn = configBeanClass.getAnnotation(Configuration.class);
            if (configAnn == null) {
                continue;
            }
            classes.add(name);
        }

        ConfigurationBeanClassParser configurationClassParser = new ConfigurationBeanClassParser(beanFactory, classes);
        Set<String> configBeanNames = configurationClassParser.parse();

        LOGGER.trace("flora scanning @Configuration Bean");
        for (String configBeanDefName : configBeanNames) {
            LOGGER.trace("scan Configuration Bean [{}]", configBeanDefName);

            BeanDefinition configBeanDef = beanFactory.getBeanDefinition(configBeanDefName);
            Class<?> configBeanClass = configBeanDef.getBeanClass();
            Configuration configAnn = configBeanClass.getAnnotation(Configuration.class);
            if (configAnn == null) {
                continue;
            }

            // 生成代理 config bean def
            Class<?> configurationProxyClass = getProxyClass(beanFactory, configBeanClass);

            configBeanDef.setBeanClass(configurationProxyClass);

            for (Method method : configBeanClass.getMethods()) {
                Bean beanAnn = method.getAnnotation(Bean.class);
                if (beanAnn == null) {
                    continue;
                }

                Class<?> beanClass = method.getReturnType();
                BeanDefinition beanDef = new BeanDefinition(beanClass);
                beanDef.setConfigurationClassBeanName(configBeanDefName);
                beanDef.setFactoryMethod(method);

                String beanName = ComponentUtil.determineBeanName(method, beanAnn);

                ComponentUtil.determineBeanScope(method, beanDef);
                ComponentUtil.determineBeanInitMethodAndDestroyMethod(beanDef);

                if (beanFactory.containsBeanDefinition(beanName)) {
                    throw new BeansException("Duplicate beanName [" + beanName + "] is not allowed");
                }
                beanFactory.registerBeanDefinition(beanName, beanDef);
            }


        }
        LOGGER.trace("flora finished scanning @Configuration Bean");
    }


    private Class<?> getProxyClass(DefaultListableBeanFactory beanFactory, Class<?> configBeanClass) {
        return getProxyClassUsingCglib(beanFactory, configBeanClass);
    }

    private Class getProxyClassUsingCglib(DefaultListableBeanFactory beanFactory, Class<?> configBeanClass) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(configBeanClass);
        enhancer.setInterfaces(configBeanClass.getInterfaces());
        enhancer.setCallbackType(ConfigurationBeanCglibMethodInterceptor.class);
        Class enhancedClass = enhancer.createClass();
        Enhancer.registerStaticCallbacks(enhancedClass, new Callback[]{new ConfigurationBeanCglibMethodInterceptor(beanFactory)});
        return enhancedClass;
    }
}
