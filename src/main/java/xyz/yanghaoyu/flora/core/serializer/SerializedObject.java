package xyz.yanghaoyu.flora.core.serializer;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/11/21 14:58<i/>
 * @version 1.0
 */


public interface SerializedObject {
    Object getObject();

    void setObject(Object o);
}
