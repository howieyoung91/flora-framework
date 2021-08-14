package xyz.yanghaoyu.flora.exception;


public class DuplicateScopeDecalarationExpcetion extends BeansException {
    public DuplicateScopeDecalarationExpcetion() {
    }

    public DuplicateScopeDecalarationExpcetion(String message) {
        super(message);
    }

    public DuplicateScopeDecalarationExpcetion(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateScopeDecalarationExpcetion(Throwable cause) {
        super(cause);
    }

    public DuplicateScopeDecalarationExpcetion(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
