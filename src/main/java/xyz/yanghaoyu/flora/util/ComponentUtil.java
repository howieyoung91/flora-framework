package xyz.yanghaoyu.flora.util;

import xyz.yanghaoyu.flora.annotation.*;
import xyz.yanghaoyu.flora.core.beans.factory.config.BeanDefinition;
import xyz.yanghaoyu.flora.core.beans.factory.support.DisposableBean;
import xyz.yanghaoyu.flora.core.beans.factory.support.InitializingBean;
import xyz.yanghaoyu.flora.exception.DuplicateDeclarationException;

import java.lang.reflect.Method;
import java.util.Objects;


public abstract class ComponentUtil {
    /**
     * Parse bean definition.
     *
     * @param clazz the clazz
     * @return the bean definition
     */
    public static BeanDefinition parse(Class<?> clazz) {
        if (!clazz.isAnnotationPresent(Component.class) && !clazz.isAnnotationPresent(Configuration.class)) {
            return null;
        }
        Component     componentAnn = clazz.getAnnotation(Component.class);
        Configuration configAnn    = clazz.getAnnotation(Configuration.class);
        if (componentAnn != null && configAnn != null) {
            throw new DuplicateDeclarationException("duplicate declaration [@Component] and [@Configuration] on class [" + clazz.getSimpleName() + "]");
        }
        BeanDefinition beanDefinition = new BeanDefinition(clazz);
        determineBeanScope(beanDefinition);
        // determineBeanInitMethodAndDestroyMethod(beanDefinition);
        return beanDefinition;
    }

    /**
     * Determine bean name string.
     *
     * @param beanDefinition the bean definition
     * @return the string
     */
    public static String determineBeanName(BeanDefinition beanDefinition) {
        Class<?>      beanClass    = beanDefinition.getBeanClass();
        Component     componentAnn = beanClass.getAnnotation(Component.class);
        Configuration configAnn    = beanClass.getAnnotation(Configuration.class);
        if (componentAnn != null && configAnn != null) {
            throw new DuplicateDeclarationException("the duplicate declaration [@Component] and [@Configuration] on " + beanDefinition.getBeanClass());
        }
        String value = null;
        if (componentAnn != null) {
            value = componentAnn.value();
        } else if (configAnn != null) {
            value = configAnn.value();
        } else {
            throw new RuntimeException();
        }
        // ???????????????????????????????????? id
        if (Objects.equals(value, Component.DEFAULT_BEAN_NAME)) {
            value = StringUtil.lowerFirstChar(beanClass.getSimpleName());
        }
        return value;
    }

    /**
     * Determine bean init method and destroy method.
     *
     * @param beanDefinition the bean definition
     */
    public static void determineBeanInitMethodAndDestroyMethod(BeanDefinition beanDefinition) {
        Class<?> beanClass = beanDefinition.getBeanClass();
        Method[] methods   = beanClass.getMethods();

        boolean isImplInitBean       = false;
        boolean isImplDisposableBean = false;
        if (InitializingBean.class.isAssignableFrom(beanClass)) {
            isImplInitBean = true;
        }
        if (DisposableBean.class.isAssignableFrom(beanClass)) {
            isImplDisposableBean = true;
        }
        int a = 0, b = 0;
        for (Method method : methods) {
            Life.Initialize initAnn = method.getAnnotation(Life.Initialize.class);
            if (initAnn != null) {
                if (a > 0) {
                    throw new IllegalArgumentException("find too many init method in class [" + beanClass.getSimpleName() + "]");
                }
                if (isImplInitBean) {
                    throw new IllegalStateException(
                            "class [" + beanDefinition.getBeanClass().getSimpleName() + "] impls interface [InitializingBean] but find the annotation [@Life.Initialize] at method [" + method + "]"
                    );
                }

                if (method.getParameterCount() == 0) {
                    beanDefinition.setInitMethodName(method.getName());
                    a++;
                } else {
                    throw new IllegalArgumentException("init method [" + beanClass.getSimpleName() + "#" + method.getName() + "] cannot receive args");
                }
            }

            Life.Destroy destroyAnn = method.getAnnotation(Life.Destroy.class);
            if (destroyAnn != null) {
                if (b > 0) {
                    throw new IllegalArgumentException("find too many destroy method in " + beanClass.getSimpleName());
                }
                if (isImplDisposableBean) {
                    throw new IllegalStateException(
                            "class [" + beanDefinition.getBeanClass().getSimpleName() + "] impls interface [DisposableBean] but find the annotation [@Life.Destroy] at method [" + method + "]"
                    );
                }

                if (method.getParameterCount() == 0) {
                    beanDefinition.setDestroyMethodName(method.getName());
                    b++;
                } else {
                    throw new IllegalArgumentException("destroy method [" + beanClass.getSimpleName() + "#" + method.getName() + "] cannot receive args in class [" + beanClass.getSimpleName() + "]");
                }
            }

        }
    }

    /**
     * Determine bean name string when using FactoryMethod(@Bean)
     *
     * @param factoryMethod the factory method
     * @param beanAnn       the @Bean Annotation
     * @return beanName
     */
    public static String determineBeanName(Method factoryMethod, Bean beanAnn) {
        String beanName = beanAnn.value();
        if (Objects.equals(beanName, Component.DEFAULT_BEAN_NAME)) {
            // ??????????????????????????? beanName
            beanName = factoryMethod.getName();
        }
        return beanName;
    }

    public static void determineBeanScope(Method factoryMethod, BeanDefinition beanDefinition) {
        Scope.Singleton singletonAnno = factoryMethod.getAnnotation(Scope.Singleton.class);
        Scope.Prototype prototypeAnno = factoryMethod.getAnnotation(Scope.Prototype.class);
        if (singletonAnno == null) {
            if (prototypeAnno != null) {
                beanDefinition.setScope(Scope.PROTOTYPE);
            }
        } else {
            if (prototypeAnno == null) {
                beanDefinition.setScope(Scope.SINGLETON);
            } else {
                throw new DuplicateDeclarationException("there are two [Scope] annotation at " + factoryMethod.getName());
            }
        }
    }

    /**
     * Determine bean scope when using @Component
     *
     * @param beanDefinition the bean definition
     */
    public static void determineBeanScope(BeanDefinition beanDefinition) {
        Class<?>        aClass        = beanDefinition.getBeanClass();
        Scope.Singleton singletonAnno = aClass.getAnnotation(Scope.Singleton.class);
        Scope.Prototype prototypeAnno = aClass.getAnnotation(Scope.Prototype.class);
        if (singletonAnno == null) {
            if (prototypeAnno != null) {
                beanDefinition.setScope(Scope.PROTOTYPE);
            }
        } else {
            if (prototypeAnno == null) {
                beanDefinition.setScope(Scope.SINGLETON);
            } else {
                throw new DuplicateDeclarationException("there are two [Scope] annotation at " + aClass.getSimpleName());
            }
        }
    }
}
