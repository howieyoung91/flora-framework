package xyz.yanghaoyu.flora.core.io.reader;

import xyz.yanghaoyu.flora.BeansException;
import xyz.yanghaoyu.flora.annotation.Component;
import xyz.yanghaoyu.flora.annotation.Inject;
import xyz.yanghaoyu.flora.beans.factory.PropertyValue;
import xyz.yanghaoyu.flora.beans.factory.config.BeanDefinition;
import xyz.yanghaoyu.flora.beans.factory.config.BeanReference;
import xyz.yanghaoyu.flora.beans.factory.support.BeanDefinitionRegistry;
import xyz.yanghaoyu.flora.core.io.container.ClassContainer;
import xyz.yanghaoyu.flora.core.io.scanner.ComponentScanner;
import xyz.yanghaoyu.flora.util.StringUtil;

import java.lang.reflect.Field;

/**
 * @author <a href="https://www.yanghaoyu.xyz">Howie Young</a><i>on 2021/8/8 20:23<i/>
 * @version 1.0
 */

public class DefaultBeanDefinitionAnnotationReader extends AbstractBeanDefinitionAnnotationReader {

    public DefaultBeanDefinitionAnnotationReader(BeanDefinitionRegistry registry) {
        super(registry);
    }

    @Override
    public void loadBeanDefinitions(String pkg) throws BeansException {
        ComponentScanner componentScanner = new ComponentScanner(pkg);
        ClassContainer classContainer = componentScanner.scan().getClassContainer();
        for (Class aClass : classContainer) {
            Component component = (Component) aClass.getAnnotation(Component.class);
            BeanDefinition beanDefinition = new BeanDefinition(aClass);
            String name = component.value();
            String id = component.id();
            // 处理id
            if (StringUtil.isEmpty(id)) {
                id = StringUtil.isEmpty(name)
                        ? StringUtil.firstLowerCase(aClass.getSimpleName())
                        : name;
            }
            Field[] fields = aClass.getDeclaredFields();

            // analyze dependency
            for (Field field : fields) {
                // 目前支持ByName
                // TODO ByType
                Inject.ByName byNameAnno = field.getAnnotation(Inject.ByName.class);
                if (byNameAnno == null) {
                    continue;
                }
                String injectBeanName = byNameAnno.value();
                String injectBeanId = byNameAnno.id();
                if (StringUtil.isEmpty(injectBeanId)) {
                    if (StringUtil.isEmpty(injectBeanName)) {
                        injectBeanId = StringUtil.firstLowerCase(field.getType().getSimpleName());
                    } else {
                        injectBeanId = injectBeanName;
                    }
                }

                // add propertyValue to beanDefinition
                PropertyValue propertyValue =
                        new PropertyValue(field.getName(), new BeanReference(injectBeanId));
                beanDefinition.getPropertyValues().addPropertyValue(propertyValue);
            }
            // register beanDefinition
            getRegistry().registerBeanDefinition(id, beanDefinition);
        }
    }
}
