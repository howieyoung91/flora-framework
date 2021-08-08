package xyz.yanghaoyu.flora.exception;

public class BeanDefinitionOverrideException extends BeansException {
    public BeanDefinitionOverrideException() {
    }

    public BeanDefinitionOverrideException(String message) {
        super(message);
    }

    public BeanDefinitionOverrideException(String message, Throwable cause) {
        super(message, cause);
    }

    public BeanDefinitionOverrideException(Throwable cause) {
        super(cause);
    }

    public BeanDefinitionOverrideException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
