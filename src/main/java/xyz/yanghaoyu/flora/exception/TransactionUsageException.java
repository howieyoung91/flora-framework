/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.exception;

public class TransactionUsageException extends TransactionException {
    public TransactionUsageException() {
    }

    public TransactionUsageException(String message) {
        super(message);
    }

    public TransactionUsageException(String message, Throwable cause) {
        super(message, cause);
    }

    public TransactionUsageException(Throwable cause) {
        super(cause);
    }

    public TransactionUsageException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
