package xyz.yanghaoyu.flora.core.io.container;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Consumer;

public class DefaultClassContainer implements ClassContainer {
    Set<Class> classSet = new HashSet<>();

    @Override
    public void add(Class... classes) {
        Collections.addAll(classSet, classes);
    }

    @Override
    public void remove(Class... classes) {
        for (Class aClass : classes) {
            classSet.remove(aClass);
        }
    }

    @Override
    public boolean contain(Class clazz) {
        return classSet.contains(clazz);
    }

    @Override
    public Iterator<Class> iterator() {
        return classSet.iterator();
    }

    @Override
    public void forEach(Consumer action) {
        classSet.forEach(action);
    }
}
