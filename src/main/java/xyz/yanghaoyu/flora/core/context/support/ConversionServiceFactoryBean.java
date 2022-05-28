package xyz.yanghaoyu.flora.core.context.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.yanghaoyu.flora.core.beans.factory.BeanFactory;
import xyz.yanghaoyu.flora.core.beans.factory.BeanFactoryAware;
import xyz.yanghaoyu.flora.core.beans.factory.ConfigurableListableBeanFactory;
import xyz.yanghaoyu.flora.core.beans.factory.FactoryBean;
import xyz.yanghaoyu.flora.core.beans.factory.config.BeanDefinition;
import xyz.yanghaoyu.flora.core.beans.factory.support.InitializingBean;
import xyz.yanghaoyu.flora.core.convert.converter.*;
import xyz.yanghaoyu.flora.core.convert.support.DefaultConversionService;
import xyz.yanghaoyu.flora.exception.BeansException;

/**
 * 类型转换
 *
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/11/21 17:57<i/>
 * @version 1.0
 */

public class ConversionServiceFactoryBean implements FactoryBean<ConversionService>, InitializingBean, BeanFactoryAware {
    private static final Logger                          LOGGER = LoggerFactory.getLogger(ConversionServiceFactoryBean.class);
    private              ConfigurableListableBeanFactory beanFactory;
    private              GenericConversionService        conversionService;

    @Override
    public ConversionService getObject() throws Exception {
        return conversionService;
    }

    @Override
    public Class<?> getObjectType() {
        return GenericConversionService.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() {
        this.conversionService = new DefaultConversionService();
        registerConverters();
    }

    private void registerConverters() {
        String[] beanNames = beanFactory.getBeanDefinitionNames();
        for (String beanName : beanNames) {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
            Class<?>       beanClass      = beanDefinition.getBeanClass();
            if (GenericConverter.class.isAssignableFrom(beanClass)) {
                GenericConverter bean = (GenericConverter) beanFactory.getBean(beanName);
                LOGGER.trace("register [GenericConverter] [{}]", beanName);
                conversionService.addConverter(bean);
            } else if (Converter.class.isAssignableFrom(beanClass)) {
                Converter bean = (Converter) beanFactory.getBean(beanName);
                LOGGER.trace("register [Converter] [{}]", beanName);
                conversionService.addConverter(bean);
            } else if (ConverterFactory.class.isAssignableFrom(beanClass)) {
                ConverterFactory<?, ?> bean = (ConverterFactory<?, ?>) beanFactory.getBean(beanName);
                LOGGER.trace("register [ConverterFactory] [{}]", beanName);
                conversionService.addConverterFactory(bean);
            }
        }
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
    }
}
