/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.framework.exception;


public class DuplicateDeclarationException extends BeansException {
    public DuplicateDeclarationException() { }

    public DuplicateDeclarationException(String message) {
        super(message);
    }

    public DuplicateDeclarationException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateDeclarationException(Throwable cause) {
        super(cause);
    }

    public DuplicateDeclarationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
