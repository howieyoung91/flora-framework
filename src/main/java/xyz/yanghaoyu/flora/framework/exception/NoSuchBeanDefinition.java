/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.framework.exception;

import cn.hutool.core.bean.BeanException;

public class NoSuchBeanDefinition extends BeanException {
    public NoSuchBeanDefinition(Throwable e) {
        super(e);
    }

    public NoSuchBeanDefinition(String message) {
        super(message);
    }

    public NoSuchBeanDefinition(String messageTemplate, Object... params) {
        super(messageTemplate, params);
    }

    public NoSuchBeanDefinition(String message, Throwable throwable) {
        super(message, throwable);
    }

    public NoSuchBeanDefinition(Throwable throwable, String messageTemplate, Object... params) {
        super(throwable, messageTemplate, params);
    }
}
