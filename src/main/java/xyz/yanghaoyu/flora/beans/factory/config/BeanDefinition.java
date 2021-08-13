package xyz.yanghaoyu.flora.beans.factory.config;

import xyz.yanghaoyu.flora.beans.factory.PropertyValues;

/**
 * @author <a href="https://www.yanghaoyu.xyz">Howie Young</a><i>on 2021/8/7 20:41<i/>
 * @version 1.0
 */


public class BeanDefinition {
    public static final String SCOPE_SINGLETON = ConfigurableBeanFactory.SCOPE_SINGLETON;
    public static final String SCOPE_PROTOTYPE = ConfigurableBeanFactory.SCOPE_PROTOTYPE;
    private Class beanClass;
    private PropertyValues propertyValues;
    private String initMethodName;
    private String destroyMethodName;
    private boolean singleton = true;
    private boolean prototype = false;

    public BeanDefinition(Class beanClass) {
        this.beanClass = beanClass;
        this.propertyValues = new PropertyValues();
    }

    public BeanDefinition(Class beanClass, PropertyValues propertyValues) {
        this.beanClass = beanClass;
        this.propertyValues = propertyValues == null ? new PropertyValues() : propertyValues;
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
        if (SCOPE_PROTOTYPE.equals(beanScope)) {
            this.prototype = true;
            this.singleton = false;
        }
    }

    public boolean isPrototype() {
        return prototype;
    }
}
