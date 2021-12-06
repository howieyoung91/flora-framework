package xyz.yanghaoyu.flora.core.context.support;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/12/6 20:01<i/>
 * @version 1.0
 */


public abstract class AbstractRefreshableAnnotationConfigApplicationContext extends AbstractRefreshableApplicationContext {
    private Class<?>[] startConfigurationClasses;

    protected Class<?>[] getStartConfigurationClasses() {
        return startConfigurationClasses;
    }

    protected void setStartConfigurationClasses(Class<?>[] startConfigurationClasses) {
        this.startConfigurationClasses = startConfigurationClasses;
    }

    protected void setStartConfigurationClasses(Class<?> startConfigurationClass) {
        setStartConfigurationClasses(new Class[]{startConfigurationClass});
    }
}
