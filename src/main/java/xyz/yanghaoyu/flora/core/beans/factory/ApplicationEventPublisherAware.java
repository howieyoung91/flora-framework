/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.core.beans.factory;

import xyz.yanghaoyu.flora.core.context.ApplicationEventPublisher;

public interface ApplicationEventPublisherAware extends Aware {
    void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher);
}
