/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.transaction;

import com.sun.istack.internal.Nullable;

import java.io.Serializable;

public class RollbackRule implements Serializable {
    private final String exceptionName;

    public RollbackRule(Class<?> clazz) {
        this.exceptionName = clazz.getName();
    }

    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        } else if (!(other instanceof RollbackRuleAttribute)) {
            return false;
        } else {
            RollbackRuleAttribute rhs = (RollbackRuleAttribute) other;
            return this.exceptionName.equals(rhs.exceptionName);
        }
    }

    public int hashCode() {
        return this.exceptionName.hashCode();
    }

    public String toString() {
        return "RollbackRuleAttribute with pattern [" + this.exceptionName + "]";
    }
}
