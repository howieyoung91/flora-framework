package xyz.yanghaoyu.flora.structure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/11/12 21:46<i/>
 * @version 1.0
 */


public class Chain {
    protected Collection<Point> points = new ArrayList<>();
    protected Iterator<Point> iterator = null;

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
}
