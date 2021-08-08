package xyz.yanghaoyu.flora.exception;

import cn.hutool.core.bean.BeanException;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2022/2/7 21:53<i/>
 * @version 1.0
 */


public class BeanCreateException extends BeanException {
    public BeanCreateException(Throwable e) {
        super(e);
    }

    public BeanCreateException(String message) {
        super(message);
    }

    public BeanCreateException(String messageTemplate, Object... params) {
        super(messageTemplate, params);
    }

    public BeanCreateException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public BeanCreateException(Throwable throwable, String messageTemplate, Object... params) {
        super(throwable, messageTemplate, params);
    }
}
