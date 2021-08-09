package xyz.yanghaoyu.flora.core.io.container;

/**
 * @author <a href="https://www.yanghaoyu.xyz">Howie Young</a><i>on 2021/8/8 21:15<i/>
 * @version 1.0
 */


public interface ClassContainer extends Iterable<Class> {
    void add(Class... classes);

    void remove(Class... classes);

    boolean contain(Class clazz);
}
