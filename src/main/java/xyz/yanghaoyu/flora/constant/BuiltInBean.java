package xyz.yanghaoyu.flora.constant;

import xyz.yanghaoyu.flora.core.aop.autoproxy.DefaultAdvisorAutoProxyCreator;
import xyz.yanghaoyu.flora.core.aop.autoproxy.annotation.AnnotationAwareAspectJAutoProxyCreator;
import xyz.yanghaoyu.flora.core.beans.factory.PropertyPlaceholderConfigurer;
import xyz.yanghaoyu.flora.core.beans.factory.config.AutowiredAnnotationBeanPostProcessor;
import xyz.yanghaoyu.flora.core.beans.factory.config.ConfigurationBeanBeanFactoryPostProcessor;
import xyz.yanghaoyu.flora.core.context.support.ConversionServiceFactoryBean;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/11/22 10:36<i/>
 * @version 1.0
 */


public interface BuiltInBean {
    Class CONVERTER_FACTORY_BEAN = ConversionServiceFactoryBean.class;
    Class DEFAULT_ADVISOR_AUTO_PROXY_CREATOR = DefaultAdvisorAutoProxyCreator.class;
    Class AUTOWIRED_ANNOTATION_BEAN_POST_PROCESSOR = AutowiredAnnotationBeanPostProcessor.class;
    Class ANNOTATION_AWARE_ASPECT_J_AUTO_PROXY_CREATOR = AnnotationAwareAspectJAutoProxyCreator.class;
    Class PROPERTY_PLACEHOLDER_CONFIGURER = PropertyPlaceholderConfigurer.class;
    Class CONFIGURATION_BEAN_BEAN_FACTORY_POST_PROCESSOR = ConfigurationBeanBeanFactoryPostProcessor.class;
}
