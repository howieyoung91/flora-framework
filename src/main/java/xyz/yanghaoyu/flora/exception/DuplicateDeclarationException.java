package xyz.yanghaoyu.flora.exception;


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
