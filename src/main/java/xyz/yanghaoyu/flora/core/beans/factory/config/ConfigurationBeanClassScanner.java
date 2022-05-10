package xyz.yanghaoyu.flora.core.beans.factory.config;

import xyz.yanghaoyu.flora.annotation.Configuration;
import xyz.yanghaoyu.flora.annotation.Enable;
import xyz.yanghaoyu.flora.annotation.Import;
import xyz.yanghaoyu.flora.core.beans.factory.support.DefaultListableBeanFactory;
import xyz.yanghaoyu.flora.core.context.annotation.ClassPathBeanDefinitionScanner;
import xyz.yanghaoyu.flora.core.io.loader.DefaultResourceLoader;
import xyz.yanghaoyu.flora.core.io.reader.XmlBeanDefinitionReader;
import xyz.yanghaoyu.flora.core.io.spi.FloraFactoriesLoader;
import xyz.yanghaoyu.flora.util.ComponentUtil;
import xyz.yanghaoyu.flora.util.IocUtil;
import xyz.yanghaoyu.flora.util.StringUtil;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 这个类扫描 被 @Configuration 标记的类
 * ConfigurationBeanClassParser#parse 会从给出的配置类中解析出其他被包含的配置类
 *
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/12/6 13:55<i/>
 * @version 1.0
 */

public class ConfigurationBeanClassScanner {
    private final DefaultListableBeanFactory beanFactory;
    private final Set<String>                startBeanNames;
    // 正在扫描的 Config Bean 用于打破递归
    private final Set<String>                current
            = new HashSet<>(6);
    // 已经完成的 Config Bean
    private final Set<String>                already
            = new HashSet<>(6);
    // 新添加的 Config Bean
    private final Set<String>                waitingConfigBeanNames
            = new HashSet<>(0);

    public ConfigurationBeanClassScanner(DefaultListableBeanFactory beanFactory, Set<String> startClasses) {
        this.beanFactory = beanFactory;
        this.startBeanNames = startClasses;
    }

    public Set<String> scan() {
        do {
            for (String startClass : startBeanNames) {
                scan(startClass, beanFactory.getBeanDefinition(startClass));
            }
        } while (transfer());

        return already;
    }

    public boolean transfer() {
        if (waitingConfigBeanNames.isEmpty()) {
            return false;
        }
        waitingConfigBeanNames.removeIf(beanName -> already.contains(beanName) || current.contains(beanName));
        startBeanNames.clear();
        startBeanNames.addAll(waitingConfigBeanNames);
        waitingConfigBeanNames.clear();
        return true;
    }

    private void scan(String beanDefName, BeanDefinition configBeanDef) {
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
        parsePropertyPlaceholder(clazz);
        parseImportConfiguration(clazz);
        parseImportResources(clazz);
        parseEnableAutoConfiguration(clazz);
        current.remove(beanDefName);
        already.add(beanDefName);
    }

    boolean parsed = false;

    // 解析 META-INF/flora.yaml
    private void parseEnableAutoConfiguration(Class<?> clazz) {
        if (clazz.isAnnotationPresent(Enable.AutoConfiguration.class)) {
            if (parsed) {
                return;
            }
            Map<String, Map<String, String>> factories =
                    FloraFactoriesLoader.loadFactories(null);
            try {
                for (Map<String, String> factory : factories.values()) {
                    String classes = factory.get("flora.framework.autoconfiguration");
                    if (classes == null) {
                        continue;
                    }
                    String[] classNames = StringUtil.commaDelimitedListToStringArray(classes);
                    for (String className : classNames) {
                        className = className.trim();
                        Class<?>      configClass = Class.forName(className);
                        Configuration configAnn   = configClass.getAnnotation(Configuration.class);
                        if (configAnn == null) {
                            continue;
                        }
                        // 注入配置
                        BeanDefinition beanDef = ComponentUtil.parse(configClass);
                        assert beanDef != null;
                        String beanName = ComponentUtil.determineBeanName(beanDef);
                        beanFactory.registerBeanDefinition(beanName, beanDef);
                        waitingConfigBeanNames.add(beanName);
                    }
                }
                parsed = true;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void parseImportResources(Class<?> clazz) {
        Import.Resource importResourceAnn = clazz.getAnnotation(Import.Resource.class);
        if (importResourceAnn != null) {
            String[]                resource = importResourceAnn.resources();
            XmlBeanDefinitionReader reader   = new XmlBeanDefinitionReader(beanFactory, new DefaultResourceLoader());
            reader.setShouldRegister(beanName -> beanDefinition -> beanDefinitionRegistry -> {
                // 添加到 waitingConfigBeanNames
                if (beanDefinition.getBeanClass().isAnnotationPresent(Configuration.class)) {
                    waitingConfigBeanNames.add(beanName);
                }
                // 不判断是否存在 直接添加
                return true;
            });
            reader.loadBeanDefinitions(resource);
        }
    }


    private void parsePropertyPlaceholder(Class<?> clazz) {
        Enable.PropertySource propertyPlaceholderAnn = clazz.getAnnotation(Enable.PropertySource.class);
        if (propertyPlaceholderAnn != null) {
            String[] location = propertyPlaceholderAnn.location();
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
                String        newBeanName = ComponentUtil.determineBeanName(newBeanDef);
                Configuration configAnn   = (Configuration) newBeanDef.getBeanClass().getAnnotation(Configuration.class);
                if (configAnn != null && !startBeanNames.contains(newBeanName)) {
                    waitingConfigBeanNames.add(newBeanName);
                }
                // if (beanFactory.containsBeanDefinition(newBeanName)) {
                //     throw new BeanDefinitionOverrideException("the bean [" + newBeanName + "] cannot be registered because it will override the old bean");
                // }
                // 这里会覆盖掉以前的 BeanDefinition
                beanFactory.registerBeanDefinition(newBeanName, newBeanDef);
            }
        }
    }

    private void parseImportConfiguration(Class<?> clazz) {
        Import.Configuration importConfigAnn = clazz.getAnnotation(Import.Configuration.class);
        if (importConfigAnn != null) {
            for (Class aClass : importConfigAnn.configuration()) {
                if (!aClass.isAnnotationPresent(Configuration.class)) {
                    continue;
                }
                BeanDefinition newBeanDef = ComponentUtil.parse(aClass);
                if (newBeanDef == null) {
                    continue;
                }
                String newBeanName = ComponentUtil.determineBeanName(newBeanDef);
                if (already.contains(newBeanName) || current.contains(newBeanName) || startBeanNames.contains(newBeanName)) {
                    continue;
                }
                beanFactory.registerBeanDefinition(newBeanName, newBeanDef);
                waitingConfigBeanNames.add(newBeanName);
            }
        }
    }
}
