/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.framework.exception;

import cn.hutool.core.bean.BeanException;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2022/2/6 16:27<i/>
 * @version 1.0
 */


public class BeanCompareException extends BeanException {
    public BeanCompareException(Throwable e) {
        super(e);
    }

    public BeanCompareException(String message) {
        super(message);
    }

    public BeanCompareException(String messageTemplate, Object... params) {
        super(messageTemplate, params);
    }

    public BeanCompareException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public BeanCompareException(Throwable throwable, String messageTemplate, Object... params) {
        super(throwable, messageTemplate, params);
    }
}
