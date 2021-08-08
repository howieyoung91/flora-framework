package xyz.yanghaoyu.flora.core.context.support;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/12/6 20:13<i/>
 * @version 1.0
 */
public class AnnotationConfigApplicationContext extends AbstractAnnotationConfigApplicationContext {
    public AnnotationConfigApplicationContext(Class... classes) {
        super(classes);
        refresh();
    }
}
