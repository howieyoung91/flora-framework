package xyz.yanghaoyu.flora.core.context.support;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/12/6 20:01<i/>
 * @version 1.0
 */


public abstract class AbstractRefreshableAnnotationConfigApplicationContext extends AbstractRefreshableApplicationContext {
    private Class<?>[] baseConfigurationClasses;

    protected void setBaseConfigurationClasses(Class<?>[] baseConfigurationClasses) {
        this.baseConfigurationClasses = baseConfigurationClasses;
    }

    protected void setBaseConfigurationClasses(Class<?> startConfigurationClass) {
        setBaseConfigurationClasses(new Class[]{startConfigurationClass});
    }

    protected Class<?>[] getBaseConfigurationClasses() {
        return baseConfigurationClasses;
    }
}
