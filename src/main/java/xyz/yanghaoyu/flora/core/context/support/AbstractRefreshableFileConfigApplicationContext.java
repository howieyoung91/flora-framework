package xyz.yanghaoyu.flora.core.context.support;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/11/26 12:45<i/>
 * @version 1.0
 */

public abstract class AbstractRefreshableFileConfigApplicationContext extends AbstractRefreshableApplicationContext {
    private String[] configLocations;

    public void setConfigLocations(String... configLocations) {
        this.configLocations = configLocations;
    }

    public void setConfigLocations(String configLocations) {
        this.configLocations = new String[]{configLocations};
    }

    protected String[] getConfigLocations() {
        return configLocations;
    }
}
