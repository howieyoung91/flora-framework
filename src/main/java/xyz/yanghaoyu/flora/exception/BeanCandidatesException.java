package xyz.yanghaoyu.flora.exception;

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
