package xyz.yanghaoyu.flora.core.beans.factory.config;

import xyz.yanghaoyu.flora.annotation.Configuration;
import xyz.yanghaoyu.flora.annotation.Enable;
import xyz.yanghaoyu.flora.core.beans.factory.support.DefaultListableBeanFactory;
import xyz.yanghaoyu.flora.core.context.annotation.ClassPathBeanDefinitionScanner;
import xyz.yanghaoyu.flora.exception.BeansException;
import xyz.yanghaoyu.flora.util.ComponentUtil;
import xyz.yanghaoyu.flora.util.IocUtil;

import java.util.HashSet;
import java.util.Set;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/12/6 13:55<i/>
 * @version 1.0
 */

public class ConfigurationClassParser {
    private final DefaultListableBeanFactory beanFactory;
    private final Set<String> startBeanNames;
    private final Set<String> current = new HashSet<>(6);
    private final Set<String> already = new HashSet<>(6);

    public ConfigurationClassParser(DefaultListableBeanFactory beanFactory, Set<String> startClasses) {
        this.beanFactory = beanFactory;
        this.startBeanNames = startClasses;
    }

    public Set<String> parse() {
        for (String startClass : startBeanNames) {
            parse(startClass, beanFactory.getBeanDefinition(startClass));
        }
        return already;
    }

    private void parse(String beanDefName, BeanDefinition configBeanDef) {
        if (already.contains(beanDefName)) {
            return;
        }
        if (current.contains(beanDefName)) {
            return;
        }
        current.add(beanDefName);
        Class<?> clazz = configBeanDef.getBeanClass();
        parseComponentScan(clazz);

        parseAop(clazz);

        parserPropertyPlaceholder(clazz);

        // todo support @Import.Configuration
        // todo support @Import.Resource

        // Import.Configuration importConfigAnn = clazz.getAnnotation(Import.Configuration.class);
        // if (importConfigAnn != null) {
        //     Class[] classes = importConfigAnn.configClasses();
        //     for (Class aClass : classes) {
        //         Annotation configAnn = aClass.getAnnotation(Configuration.class);
        //         if (configAnn == null) {
        //             continue;
        //         }
        //
        //
        //     }
        // }

        current.remove(beanDefName);
        already.add(beanDefName);
    }

    private void parserPropertyPlaceholder(Class<?> clazz) {
        Enable.PropertyPlaceholder propertyPlaceholderAnn = clazz.getAnnotation(Enable.PropertyPlaceholder.class);
        if (propertyPlaceholderAnn != null) {
            String location = propertyPlaceholderAnn.location();
            IocUtil.enablePropertyPlaceholderConfigurer(beanFactory, location);
        }
    }

    private void parseAop(Class<?> clazz) {
        if (clazz.isAnnotationPresent(Enable.Aop.class)) {
            IocUtil.enableAop(beanFactory);
        }
    }

    private void parseComponentScan(Class<?> clazz) {
        Enable.ComponentScan componentScanAnn = clazz.getAnnotation(Enable.ComponentScan.class);
        if (componentScanAnn != null) {
            String[] basePackages = componentScanAnn.basePackages();
            // 扫包
            Set<BeanDefinition> newBeanDefs = new ClassPathBeanDefinitionScanner().doScan(basePackages);

            for (BeanDefinition newBeanDef : newBeanDefs) {

                String newBeanName = ComponentUtil.determineBeanName(newBeanDef);
                Configuration configAnn = (Configuration) newBeanDef.getBeanClass().getAnnotation(Configuration.class);
                if (configAnn != null) {
                    parse(newBeanName, newBeanDef);
                }
                if (beanFactory.containsBeanDefinition(newBeanName)) {
                    throw new BeansException("Duplicate beanName [" + newBeanName + "] is not allowed");
                }
                beanFactory.registerBeanDefinition(newBeanName, newBeanDef);
            }
        }
    }
}
