package xyz.yanghaoyu.flora.core.beans.factory.config;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import xyz.yanghaoyu.flora.annotation.Bean;
import xyz.yanghaoyu.flora.annotation.Configuration;
import xyz.yanghaoyu.flora.annotation.Inject;
import xyz.yanghaoyu.flora.core.beans.factory.ConfigurableListableBeanFactory;
import xyz.yanghaoyu.flora.core.beans.factory.support.DefaultListableBeanFactory;
import xyz.yanghaoyu.flora.exception.BeansException;
import xyz.yanghaoyu.flora.util.StringUtil;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * 对 @Configuration @Bean 做支持
 *
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/12/4 22:57<i/>
 * @version 1.0
 */

public class ConfigurationBeanBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configBeanFactory) throws BeansException {
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) configBeanFactory;
        String[] names = beanFactory.getBeanDefinitionNames();
        for (String configBeanDefName : names) {

            BeanDefinition configBeanDef = beanFactory.getBeanDefinition(configBeanDefName);
            Class<?> configBeanClass = configBeanDef.getBeanClass();

            Configuration configAnn = configBeanClass.getAnnotation(Configuration.class);
            if (configAnn == null) {
                continue;
            }

            // TODO modify the configuration beanDefinition
            for (Method method : configBeanClass.getMethods()) {
                Bean beanAnn = method.getAnnotation(Bean.class);
                if (beanAnn == null) {
                    continue;
                }

                Class<?> beanClass = method.getReturnType();
                BeanDefinition beanDef = new BeanDefinition(beanClass);

                String beanName = determineBeanName(method, beanAnn);
                // TODO @scope

                beanDef.setConfigurationClassBeanName(configBeanDefName);
                beanDef.setFactoryMethodName(method.getName());
                beanDef.setFactoryMethod(method);
                if (beanFactory.containsBeanDefinition(beanName)) {
                    throw new BeansException("Duplicate beanId [" + beanName + "] is not allowed");
                }
                beanFactory.registerBeanDefinition(beanName, beanDef);
            }

            // 生成代理 config bean def
            Object configurationProxyClass
                    = new ConfigurationClassEnhanceClassProxy(configBeanClass, beanFactory).getConfigurationProxyClass();
            //

            beanFactory.registerSingleton(configBeanDefName, configurationProxyClass);
            // configBeanDef.setBeanClass(configurationProxyClass);
        }
    }

    private static String determineBeanName(Method method, Bean beanAnn) {
        String beanName = beanAnn.value();
        if (beanName.equals("")) {
            beanName = StringUtil.lowerFirstChar(method.getName());
        }
        return beanName;
    }

    private static class ConfigurationClassEnhanceClassProxy implements MethodInterceptor {
        private final Class<?> realConfigClass;
        private final DefaultListableBeanFactory beanFactory;

        public ConfigurationClassEnhanceClassProxy(Class<?> realConfigClass, ConfigurableListableBeanFactory beanFactory) {
            this.realConfigClass = realConfigClass;
            this.beanFactory = (DefaultListableBeanFactory) beanFactory;
        }


        public Object getConfigurationProxyClass() {
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(realConfigClass);
            enhancer.setInterfaces(realConfigClass.getInterfaces());
            // enhancer.setCallbackType(ConfigurationClassEnhanceClassProxy.class);
            enhancer.setCallback(this);
            return enhancer.create();
        }

        @Override
        public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
            String beanName = determineBeanName(method, method.getAnnotation(Bean.class));
            if (!beanFactory.isCurrentlyCreating(beanName)) {
                return beanFactory.getBean(beanName);
            }
            Parameter[] parameters = method.getParameters();
            int i = 0;
            for (Parameter parameter : parameters) {
                Inject.ByName byNameAnn = parameter.getAnnotation(Inject.ByName.class);
                if (byNameAnn == null) {
                    // byType
                    Class<?> type = parameter.getType();
                    args[i] = beanFactory.getBeansOfType(type).values().iterator().next();
                } else {
                    String dependOnBeanName = byNameAnn.value();
                    if (dependOnBeanName.equals("")) {
                        dependOnBeanName = parameter.getName();
                    }
                    args[i] = beanFactory.getBean(dependOnBeanName);
                }
                i++;
            }
            // 使用真实对象生成
            return methodProxy.invokeSuper(o, args);
        }
    }
}
