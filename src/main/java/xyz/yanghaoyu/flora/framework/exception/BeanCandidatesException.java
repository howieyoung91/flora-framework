/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.framework.exception;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/12/6 12:44<i/>
 * @version 1.0
 */

public class BeanCandidatesException extends BeansException {
    public BeanCandidatesException() {
    }

    public BeanCandidatesException(String message) {
        super(message);
    }

    public BeanCandidatesException(String message, Throwable cause) {
        super(message, cause);
    }

    public BeanCandidatesException(Throwable cause) {
        super(cause);
    }

    public BeanCandidatesException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
