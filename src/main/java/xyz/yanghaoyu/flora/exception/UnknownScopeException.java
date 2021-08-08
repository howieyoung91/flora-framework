package xyz.yanghaoyu.flora.exception;


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
