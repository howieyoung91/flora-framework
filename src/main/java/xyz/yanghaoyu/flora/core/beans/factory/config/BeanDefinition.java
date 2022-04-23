package xyz.yanghaoyu.flora.core.beans.factory.config;

import xyz.yanghaoyu.flora.annotation.Scope;
import xyz.yanghaoyu.flora.core.beans.factory.PropertyValues;

import java.lang.reflect.Method;

public class BeanDefinition {
    private Class          beanClass;
    private PropertyValues propertyValues;
    private String         initMethodName;
    private String         destroyMethodName;
    private boolean        singleton = true;
    private boolean        prototype = false;

    // @Bean @Configuration support
    private String configurationClassBeanName = null;
    private Method factoryMethod              = null;

    public BeanDefinition(Class beanClass) {
        this.beanClass = beanClass;
        this.propertyValues = new PropertyValues();
    }

    public BeanDefinition(Class beanClass, PropertyValues propertyValues) {
        this.beanClass = beanClass;
        this.propertyValues = propertyValues == null ? new PropertyValues() : propertyValues;
    }

    public BeanDefinition setBeanClass(Class beanClass) {
        this.beanClass = beanClass;
        return this;
    }

    public Class getBeanClass() {
        return beanClass;
    }

    public PropertyValues getPropertyValues() {
        return propertyValues;
    }

    public String getInitMethodName() {
        return initMethodName;
    }

    public BeanDefinition setInitMethodName(String initMethodName) {
        this.initMethodName = initMethodName;
        return this;
    }

    public String getDestroyMethodName() {
        return destroyMethodName;
    }

    public BeanDefinition setDestroyMethodName(String destroyMethodName) {
        this.destroyMethodName = destroyMethodName;
        return this;
    }

    public boolean isSingleton() {
        return singleton;
    }

    public void setScope(String beanScope) {
        switch (beanScope) {
            case Scope.SINGLETON: {
                this.prototype = false;
                this.singleton = true;
                break;
            }
            case Scope.PROTOTYPE: {
                this.prototype = true;
                this.singleton = false;
                break;
            }
        }
    }


    public String getConfigurationClassBeanName() {
        return configurationClassBeanName;
    }

    public void setConfigurationClassBeanName(String configurationClassBeanName) {
        this.configurationClassBeanName = configurationClassBeanName;
    }

    public boolean isPrototype() {
        return prototype;
    }

    public Method getFactoryMethod() {
        return factoryMethod;
    }

    public void setFactoryMethod(Method factoryMethod) {
        this.factoryMethod = factoryMethod;
    }
}
