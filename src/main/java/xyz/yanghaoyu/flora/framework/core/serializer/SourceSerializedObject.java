/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.framework.core.serializer;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/11/21 14:57<i/>
 * @version 1.0
 */


public class SourceSerializedObject implements SerializedObject {
    private Object sourceObject;

    public SourceSerializedObject() {}

    public SourceSerializedObject(Object sourceObject) {
        this.sourceObject = sourceObject;
    }

    @Override
    public Object getObject() {
        return sourceObject;
    }

    @Override
    public void setObject(Object o) {
        this.sourceObject = o;
    }
}
