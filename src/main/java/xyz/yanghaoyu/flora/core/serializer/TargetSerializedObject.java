package xyz.yanghaoyu.flora.core.serializer;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/11/21 14:57<i/>
 * @version 1.0
 */


public class TargetSerializedObject implements SerializedObject {
    private Object targetObject;

    public TargetSerializedObject() {}

    public TargetSerializedObject(Object targetObject) {
        this.targetObject = targetObject;
    }

    @Override
    public Object getObject() {
        return targetObject;
    }

    @Override
    public void setObject(Object o) {
        targetObject = o;
    }
}
