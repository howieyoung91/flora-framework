/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.framework.exception;


public class UnknownScopeException extends BeansException {
    public UnknownScopeException() {
    }

    public UnknownScopeException(String message) {
        super(message);
    }

    public UnknownScopeException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnknownScopeException(Throwable cause) {
        super(cause);
    }

    public UnknownScopeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
