package xyz.yanghaoyu.flora.exception;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/12/4 21:26<i/>
 * @version 1.0
 */

public class ContextException extends RuntimeException{
    public ContextException() {
    }

    public ContextException(String message) {
        super(message);
    }

    public ContextException(String message, Throwable cause) {
        super(message, cause);
    }

    public ContextException(Throwable cause) {
        super(cause);
    }

    public ContextException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
