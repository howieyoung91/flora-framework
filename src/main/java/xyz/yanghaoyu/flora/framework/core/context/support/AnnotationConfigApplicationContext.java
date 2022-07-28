/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.framework.core.context.support;

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
