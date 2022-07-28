/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.framework.exception;

public class BeanDefinitionOverrideException extends BeansException {
    public BeanDefinitionOverrideException() {
    }

    public BeanDefinitionOverrideException(String message) {
        super(message);
    }

    public BeanDefinitionOverrideException(String message, Throwable cause) {
        super(message, cause);
    }

    public BeanDefinitionOverrideException(Throwable cause) {
        super(cause);
    }

    public BeanDefinitionOverrideException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
