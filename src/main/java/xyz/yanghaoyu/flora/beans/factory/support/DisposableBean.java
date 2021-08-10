package xyz.yanghaoyu.flora.beans.factory.support;

/**
 * @author <a href="https://www.yanghaoyu.xyz">Howie Young</a><i>on 2021/8/10 16:42<i/>
 * @version 1.0
 */


public interface DisposableBean {
    void destroy() throws Exception;
}
