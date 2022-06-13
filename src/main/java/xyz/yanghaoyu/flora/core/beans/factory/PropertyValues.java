package xyz.yanghaoyu.flora.core.beans.factory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class PropertyValues implements Iterable<PropertyValue> {
    private final List<PropertyValue> propertyValues = new ArrayList<>();

    public PropertyValues addPropertyValue(PropertyValue pv) {
        this.propertyValues.add(pv);
        return this;
    }

    public PropertyValue[] getPropertyValues() {
        return this.propertyValues.toArray(new PropertyValue[0]);
    }


    public PropertyValue getPropertyValue(String propertyName) {
        for (PropertyValue pv : propertyValues) {
            if (pv.getName().equals(propertyName)) {
                return pv;
            }
        }
        return null;
    }

    @Override
    public Iterator<PropertyValue> iterator() {
        return propertyValues.iterator();
    }

    @Override
    public void forEach(Consumer<? super PropertyValue> action) {
        propertyValues.forEach(action);
    }
}
