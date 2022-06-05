/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.transaction;

import java.io.Serializable;
import java.util.Objects;

/**
 * copied from spring
 */
public class RollbackRuleAttribute implements Serializable {
    private final Class<?> exceptionClass;

    public RollbackRuleAttribute(Class<?> clazz) {
        if (!Throwable.class.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException("Cannot construct rollback rule from [" + clazz.getName() + "]: it's not a Throwable");
        }
        exceptionClass = clazz;
    }

    public Class<?> getExceptionClass() {
        return exceptionClass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        RollbackRuleAttribute that = (RollbackRuleAttribute) o;
        return Objects.equals(exceptionClass, that.exceptionClass);
    }

    public int hashCode() {
        return this.getClass().hashCode();
    }
}
