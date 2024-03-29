/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.framework.core.beans.factory.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.yanghaoyu.flora.framework.annotation.ConditionContext;
import xyz.yanghaoyu.flora.framework.annotation.condition.FloraFrameworkConditionChain;
import xyz.yanghaoyu.flora.framework.core.Ordered;
import xyz.yanghaoyu.flora.framework.core.PriorityOrdered;
import xyz.yanghaoyu.flora.framework.core.beans.factory.ConfigurableListableBeanFactory;
import xyz.yanghaoyu.flora.framework.core.beans.factory.support.BeanDefinitionRegistry;
import xyz.yanghaoyu.flora.framework.core.beans.factory.support.DefaultListableBeanFactory;
import xyz.yanghaoyu.flora.framework.core.io.ResourceLoader;
import xyz.yanghaoyu.flora.framework.core.io.loader.DefaultResourceLoader;
import xyz.yanghaoyu.flora.framework.exception.BeansException;
import xyz.yanghaoyu.flora.framework.util.ReflectUtil;

import java.util.Iterator;
import java.util.Set;

public class ConditionalSupportPostProcessor
        implements BeanFactoryPostProcessor, PriorityOrdered {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConditionalSupportPostProcessor.class);

    private Set<String>                  skippedBeanNames;
    private FloraFrameworkConditionChain conditionChain = new FloraFrameworkConditionChain();

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE / 2 + 1;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        DefaultListableBeanFactory beanFactory      = (DefaultListableBeanFactory) configurableListableBeanFactory;
        ConditionContext           conditionContext = new FloraConditionContext(beanFactory);
        filter(beanFactory, conditionContext);
    }

    private void filter(DefaultListableBeanFactory beanFactory, ConditionContext conditionContext) {
        boolean shouldContinue = false;
        while (true) {
            Iterator<String> iterator = skippedBeanNames.iterator();
            while (iterator.hasNext()) {
                String         skippedBeanName = iterator.next();
                BeanDefinition beanDef         = beanFactory.getBeanDefinition(skippedBeanName);
                if (!conditionChain.matches(conditionContext, beanDef)) {
                    beanFactory.removeBeanDefinition(skippedBeanName);
                    iterator.remove();
                    shouldContinue = true;
                    LOGGER.trace("remove bean definition [{}]", skippedBeanName);
                }
            }
            if (!shouldContinue) {
                break;
            }
            shouldContinue = false;
        }
    }


    private static class FloraConditionContext implements ConditionContext {
        private DefaultListableBeanFactory beanFactory;
        private DefaultResourceLoader      resourceLoader = new DefaultResourceLoader();

        public FloraConditionContext(DefaultListableBeanFactory beanFactory) {
            this.beanFactory = beanFactory;
        }

        @Override
        public BeanDefinitionRegistry getRegistry() {
            return beanFactory;
        }

        @Override
        public ConfigurableListableBeanFactory getBeanFactory() {
            return beanFactory;
        }

        @Override
        public ResourceLoader getResourceLoader() {
            return resourceLoader;
        }

        @Override
        public ClassLoader getClassLoader() {
            return ReflectUtil.getDefaultClassLoader();
        }
    }

}
