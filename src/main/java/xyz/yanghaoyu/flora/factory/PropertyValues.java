package xyz.yanghaoyu.flora.factory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class PropertyValues implements Iterable<PropertyValue> {
    private final List<PropertyValue> propertyValueList = new ArrayList<>();

    public PropertyValues addPropertyValue(PropertyValue pv) {
        this.propertyValueList.add(pv);
        return this;
    }

    public PropertyValue[] getPropertyValues() {
        return this.propertyValueList.toArray(new PropertyValue[0]);
    }


    public PropertyValue getPropertyValue(String propertyName) {
        for (PropertyValue pv : this.propertyValueList) {
            if (pv.getName().equals(propertyName)) {
                return pv;
            }
        }
        return null;
    }

    @Override
    public Iterator<PropertyValue> iterator() {
        return propertyValueList.iterator();
    }

    @Override
    public void forEach(Consumer<? super PropertyValue> action) {
        propertyValueList.forEach(action);
    }
}
