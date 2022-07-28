/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.framework.core.context.support;

/**
 * @author <a href="https://www.yanghaoyu.xyz">Howie Young</a><i>on 2021/8/15 14:22<i/>
 * @version 1.0
 */


public class ContextConfig {
    private boolean enableAop;

    public boolean isEnableAop() {
        return enableAop;
    }

    public ContextConfig setEnableAop(boolean enableAop) {
        this.enableAop = enableAop;
        return this;
    }
}
