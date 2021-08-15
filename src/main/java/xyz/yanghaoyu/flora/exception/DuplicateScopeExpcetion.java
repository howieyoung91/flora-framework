package xyz.yanghaoyu.flora.exception;


public class DuplicateScopeExpcetion extends BeansException {
    public DuplicateScopeExpcetion() {
    }

    public DuplicateScopeExpcetion(String message) {
        super(message);
    }

    public DuplicateScopeExpcetion(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateScopeExpcetion(Throwable cause) {
        super(cause);
    }

    public DuplicateScopeExpcetion(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
