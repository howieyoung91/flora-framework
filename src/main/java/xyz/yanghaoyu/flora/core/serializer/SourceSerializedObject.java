package xyz.yanghaoyu.flora.core.serializer;

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
