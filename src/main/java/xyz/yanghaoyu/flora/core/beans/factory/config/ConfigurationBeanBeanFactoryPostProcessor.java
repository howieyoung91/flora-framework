package xyz.yanghaoyu.flora.core.beans.factory.config;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.yanghaoyu.flora.annotation.Bean;
import xyz.yanghaoyu.flora.annotation.Configuration;
import xyz.yanghaoyu.flora.annotation.Inject;
import xyz.yanghaoyu.flora.core.beans.factory.ConfigurableListableBeanFactory;
import xyz.yanghaoyu.flora.core.beans.factory.support.DefaultListableBeanFactory;
import xyz.yanghaoyu.flora.exception.BeanCandidatesException;
import xyz.yanghaoyu.flora.exception.BeansException;
import xyz.yanghaoyu.flora.exception.DuplicateDeclarationException;
import xyz.yanghaoyu.flora.util.StringUtil;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashSet;
import java.util.Map;
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

        ConfigurationClassParser configurationClassParser = new ConfigurationClassParser(beanFactory, classes);
        Set<String> configBeanNames = configurationClassParser.parse();

        for (String configBeanDefName : configBeanNames) {
            LOGGER.trace("scan Configuration Bean [{}]", configBeanDefName);

            Class<?> configBeanClass = beanFactory.getBeanDefinition(configBeanDefName).getBeanClass();
            Configuration configAnn = configBeanClass.getAnnotation(Configuration.class);
            if (configAnn == null) {
                continue;
            }

            // 生成代理 config bean def
            Object configurationProxyClass
                    = new ConfigurationClassEnhanceClassProxy(configBeanClass, beanFactory).getConfigurationProxyClass();
            //

            beanFactory.registerSingleton(configBeanDefName, configurationProxyClass);

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
                    throw new BeansException("Duplicate beanName [" + beanName + "] is not allowed");
                }
                beanFactory.registerBeanDefinition(beanName, beanDef);
            }
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
            enhancer.setCallback(this);
            return enhancer.create();
        }

        @Override
        public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
            String beanName = determineBeanName(method, method.getAnnotation(Bean.class));
            if (!beanFactory.isCurrentlyCreating(beanName)) {
                return beanFactory.getBean(beanName);
            }

            int i = 0;
            Parameter[] parameters = method.getParameters();
            for (Parameter parameter : parameters) {
                Inject.ByName byNameAnn = parameter.getAnnotation(Inject.ByName.class);
                Inject.ByType byTypeAnn = parameter.getAnnotation(Inject.ByType.class);
                if (byNameAnn != null && byTypeAnn != null) {
                    throw new DuplicateDeclarationException(
                            "duplicate declaration [@Inject.ByName],[@Inject.ByType] on "
                            + parameter.getName()
                            + " when creating bean [" + beanName + "]"
                    );
                }
                Object bean = getBeanFromBeanFactory(beanName, parameter, byNameAnn, byTypeAnn);
                args[i] = bean;
                i++;
            }
            // 使用真实对象生成
            return methodProxy.invokeSuper(o, args);
        }

        private Object getBeanFromBeanFactory(String beanName, Parameter parameter, Inject.ByName byNameAnn, Inject.ByType byTypeAnn) {
            Object bean;
            if (byTypeAnn != null) {
                Class<?> type = parameter.getType();
                // FIXME 依赖于和Bean相同的类型 这里就会出现循环依赖
                Map<String, ?> candidate = beanFactory.getBeansOfType(type);
                if (candidate.size() == 0) {
                    throw new BeanCandidatesException("find no candidate when creating bean [" + beanName + "]");
                }
                if (candidate.size() > 1) {
                    throw new BeanCandidatesException("find too many candidates class is" + type + " when creating bean [" + beanName + "]");
                }
                bean = candidate.values().iterator().next();
            } else {
                String dependOnBeanName = "";
                if (byNameAnn == null) {
                    dependOnBeanName = parameter.getName();
                } else {
                    dependOnBeanName = byNameAnn.value();
                    if (dependOnBeanName.equals("")) {
                        dependOnBeanName = parameter.getName();
                    }
                }
                bean = beanFactory.getBean(dependOnBeanName);
            }
            return bean;
        }
    }
}
