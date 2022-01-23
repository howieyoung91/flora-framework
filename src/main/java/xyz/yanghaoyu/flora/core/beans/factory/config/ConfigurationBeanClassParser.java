package xyz.yanghaoyu.flora.core.beans.factory.config;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.yanghaoyu.flora.annotation.Bean;
import xyz.yanghaoyu.flora.annotation.Configuration;
import xyz.yanghaoyu.flora.core.beans.factory.support.DefaultListableBeanFactory;
import xyz.yanghaoyu.flora.exception.BeansException;
import xyz.yanghaoyu.flora.util.ComponentUtil;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * 这个类用于解析被 @Configuration 标记的类
 * 并把这些类全部注册进入 beanFactory
 *
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/12/27 12:09<i/>
 * @version 1.0
 */

public class ConfigurationBeanClassParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationBeanClassParser.class);
    private final Set<String> configBeanNames;
    private final DefaultListableBeanFactory beanFactory;

    public ConfigurationBeanClassParser(DefaultListableBeanFactory beanFactory, Set<String> configBeanName) {
        this.configBeanNames = configBeanName;
        this.beanFactory = beanFactory;
    }

    public void parse() {
        for (String configBeanName : configBeanNames) {
            LOGGER.trace("parse [Configuration] [{}] ...", configBeanName);
            BeanDefinition configBeanDef = beanFactory.getBeanDefinition(configBeanName);
            Class<?> configBeanClass = configBeanDef.getBeanClass();
            Configuration configAnn = configBeanClass.getAnnotation(Configuration.class);
            if (configAnn == null) {
                continue;
            }

            // 使用 cglib 生成代理 config bean def 先从 beanFactory 中获取 bean
            Class<?> configurationProxyClass = getProxyClass(beanFactory, configBeanClass);

            configBeanDef.setBeanClass(configurationProxyClass);

            for (Method method : configBeanClass.getMethods()) {
                Bean beanAnn = method.getAnnotation(Bean.class);
                if (beanAnn == null) {
                    continue;
                }

                Class<?> beanClass = method.getReturnType();
                BeanDefinition beanDef = new BeanDefinition(beanClass);
                beanDef.setConfigurationClassBeanName(configBeanName);
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
