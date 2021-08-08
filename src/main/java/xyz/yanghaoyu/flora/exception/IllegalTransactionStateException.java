/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.exception;

public class IllegalTransactionStateException extends TransactionException {
    public IllegalTransactionStateException() {
    }

    public IllegalTransactionStateException(String message) {
        super(message);
    }

    public IllegalTransactionStateException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalTransactionStateException(Throwable cause) {
        super(cause);
    }

    public IllegalTransactionStateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
