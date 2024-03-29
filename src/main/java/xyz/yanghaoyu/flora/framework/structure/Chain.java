/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.framework.structure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Consumer;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/11/12 21:46<i/>
 * @version 1.0
 */


public class Chain {
    protected Collection<Point> points   = new ArrayList<>();
    protected Iterator<Point>   iterator = null;

    @FunctionalInterface
    public interface Point {
        Object proceed(Chain chain) throws Throwable;
    }

    public Chain() {}

    public Chain(Collection<Point> points) {
        this.points = points;
    }

    public Object proceed() throws Throwable {
        return shouldEnd() ? end() : next();
    }

    protected boolean shouldEnd() {
        if (iterator == null) {
            iterator = points.iterator();
        }
        return !iterator.hasNext();
    }

    protected Object next() throws Throwable {
        return iterator.next().proceed(this);
    }

    private Object end() throws Throwable {
        iterator = null;
        return doEnd();
    }

    protected Object doEnd() throws Throwable {
        return null;
    }

    public void addPoint(Point point) {
        points.add(point);
    }

    public void forEach(Consumer<? super Point> action) {
        points.forEach(action);
    }
}
