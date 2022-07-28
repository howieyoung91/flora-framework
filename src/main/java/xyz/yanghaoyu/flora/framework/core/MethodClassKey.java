/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.framework.core;

import xyz.yanghaoyu.flora.framework.util.ObjectUtil;

import java.lang.reflect.Method;

public final class MethodClassKey implements Comparable<MethodClassKey> {
    private final Method   method;
    private final Class<?> targetClass;

    public static MethodClassKey of(Method method, Class<?> targetClass) {
        return new MethodClassKey(method, targetClass);
    }

    public MethodClassKey(Method method, Class<?> targetClass) {
        this.method = method;
        this.targetClass = targetClass;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof MethodClassKey)) {
            return false;
        }
        MethodClassKey otherKey = (MethodClassKey) other;
        return this.method.equals(otherKey.method) && ObjectUtil.nullSafeEquals(this.targetClass, otherKey.targetClass);
    }

    @Override
    public int hashCode() {
        return this.method.hashCode() + (this.targetClass != null ? this.targetClass.hashCode() * 29 : 0);
    }

    @Override
    public String toString() {
        return this.method + (this.targetClass != null ? " on " + this.targetClass : "");
    }

    @Override
    public int compareTo(MethodClassKey other) {
        int result = this.method.getName().compareTo(other.method.getName());
        if (result == 0) {
            result = this.method.toString().compareTo(other.method.toString());
            if (result == 0 && this.targetClass != null && other.targetClass != null) {
                result = this.targetClass.getName().compareTo(other.targetClass.getName());
            }
        }
        return result;
    }
}
