package xyz.yanghaoyu.flora.constant;

import xyz.yanghaoyu.flora.core.aop.autoproxy.AnnotationAwareAspectJAutoProxyCreator;
import xyz.yanghaoyu.flora.core.aop.autoproxy.DefaultAdvisorAutoProxyCreator;
import xyz.yanghaoyu.flora.core.beans.factory.PropertyPlaceholderConfigurer;
import xyz.yanghaoyu.flora.core.beans.factory.config.*;
import xyz.yanghaoyu.flora.core.context.support.ConversionServiceFactoryBean;
import xyz.yanghaoyu.flora.transaction.config.TransactionBeanFactoryPostProcessor;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/11/22 10:36<i/>
 * @version 1.0
 */


public interface BuiltInBean {
    Class<?> CONVERTER_FACTORY_BEAN
            = ConversionServiceFactoryBean.class;
    Class<?> DEFAULT_ADVISOR_AUTO_PROXY_CREATOR
            = DefaultAdvisorAutoProxyCreator.class;
    Class<?> ANNOTATION_AWARE_ASPECT_J_AUTO_PROXY_SUPPORT_BEAN_FACTORY_POST_PROCESSOR
            = AnnotationAwareAspectJAutoProxySupportBeanFactoryPostProcessor.class;
    Class<?> AUTOWIRED_ANNOTATION_BEAN_POST_PROCESSOR
            = AutowiredAnnotationBeanPostProcessor.class;
    Class<?> ANNOTATION_AWARE_ASPECT_J_AUTO_PROXY_CREATOR
            = AnnotationAwareAspectJAutoProxyCreator.class;
    Class<?> PROPERTY_PLACEHOLDER_CONFIGURER
            = PropertyPlaceholderConfigurer.class;
    Class<?> CONFIGURATION_BEAN_BEAN_FACTORY_POST_PROCESSOR
            = ConfigurationClassBeanFactoryPostProcessor.class;
    Class<?> INIT_DESTROY_ANNOTATION_BEAN_POST_PROCESSOR
            = InitDestroyAnnotationBeanPostProcessor.class;
    Class<?> CONFIGURATION_PROPERTIES_BINDING_POST_PROCESSOR
            = ConfigurationPropertiesBindingPostProcessor.class;
    Class<?> TRANSACTION_BEAN_FACTORY_POST_PROCESSOR
            = TransactionBeanFactoryPostProcessor.class;
}
