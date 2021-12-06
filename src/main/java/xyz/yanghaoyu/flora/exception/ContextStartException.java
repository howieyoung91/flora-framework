package xyz.yanghaoyu.flora.exception;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/12/4 21:26<i/>
 * @version 1.0
 */

public class ContextStartException extends RuntimeException{
    public ContextStartException() {
    }

    public ContextStartException(String message) {
        super(message);
    }

    public ContextStartException(String message, Throwable cause) {
        super(message, cause);
    }

    public ContextStartException(Throwable cause) {
        super(cause);
    }

    public ContextStartException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
