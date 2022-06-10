package xyz.yanghaoyu.flora.util;

import xyz.yanghaoyu.flora.structure.MultiValueMap;

import java.util.*;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/11/20 16:31<i/>
 * @version 1.0
 */

public abstract class CollectionUtil {
    public static boolean isEmpty(Collection<?> collection) {
        return (collection == null || collection.isEmpty());
    }

    public static Collection newDefaultCollection(Class targetType) {
        if (targetType.isInterface()) {
            if (Set.class == targetType || AbstractCollection.class == targetType) {
                return new LinkedHashSet<>();
            }
            else if (List.class == targetType) {
                return new ArrayList<>();
            }
            else if (SortedSet.class == targetType || NavigableSet.class == targetType) {
                return new TreeSet<>();
            }
            else {
                throw new IllegalArgumentException("Unsupported Collection interface: " + targetType.getName());
            }
        }
        /*
        else if (EnumSet.class.isAssignableFrom(targetType)) {
             Objects.requireNonNull(elementType, "Cannot create EnumSet for unknown element type");
             Cast is necessary for compilation in Eclipse 4.4 .1.
             return (Collection) EnumSet.noneOf(asEnumType(elementType));
        }
         */
        else {
            if (!Collection.class.isAssignableFrom(targetType)) {
                throw new IllegalArgumentException("Unsupported Collection type: " + targetType.getName());
            }
            try {
                return (Collection) ReflectUtil.newInstanceByDefaultCtor(targetType);
                // return (Collection<Object>) ReflectionUtils.accessibleConstructor(targetType).newInstance();
            }
            catch (Throwable ex) {
                throw new IllegalArgumentException(
                        "Could not instantiate Collection type: " + targetType.getName(), ex);
            }
        }
    }

    public static <K, V> Map<K, V> createMap(Class<?> mapType,/*, Class<?> keyType*/ int capacity) {
        Objects.requireNonNull(mapType, "Map type must not be null");
        if (mapType.isInterface()) {
            if (Map.class == mapType) {
                return new LinkedHashMap<>(capacity);
            }
            else if (SortedMap.class == mapType || NavigableMap.class == mapType) {
                // 可排序的
                return new TreeMap<>();
            }
           /* else if (MultiValueMap.class == mapType) {
                return new LinkedMultiValueMap();
            } */
            else {
                throw new IllegalArgumentException("Unsupported Map interface: " + mapType.getName());
            }
        }
        /*
        else if (EnumMap.class == mapType) {
            Objects.requireNonNull(keyType, "Cannot create EnumMap for unknown key type");
            return new EnumMap(asEnumType(keyType));
        }
        */
        else {
            if (!Map.class.isAssignableFrom(mapType)) {
                throw new IllegalArgumentException("Unsupported Map type: " + mapType.getName());
            }
            try {
                return (Map<K, V>) ReflectUtil.newInstanceByDefaultCtor(mapType);
            }
            catch (Throwable ex) {
                throw new IllegalArgumentException("Could not instantiate Map type: " + mapType.getName(), ex);
            }
        }
    }

    public static Collection newCollection(Class targetType, int cap) {
        if (targetType.isInterface()) {
            if (Set.class == targetType || AbstractCollection.class == targetType) {
                return new LinkedHashSet<>(cap);
            }
            else if (List.class == targetType) {
                return new ArrayList<>(cap);
            }
            else if (SortedSet.class == targetType || NavigableSet.class == targetType) {
                return new TreeSet<>();
            }
            else {
                throw new IllegalArgumentException("Unsupported Collection interface: " + targetType.getName());
            }
        }
        /*
        else if (EnumSet.class.isAssignableFrom(targetType)) {
             Objects.requireNonNull(elementType, "Cannot create EnumSet for unknown element type");
             Cast is necessary for compilation in Eclipse 4.4 .1.
             return (Collection) EnumSet.noneOf(asEnumType(elementType));
        }
         */
        else {
            if (!Collection.class.isAssignableFrom(targetType)) {
                throw new IllegalArgumentException("Unsupported Collection type: " + targetType.getName());
            }
            try {
                // 使用默认构造函数
                return (Collection) ReflectUtil.newInstanceByDefaultCtor(targetType);
            }
            catch (Throwable ex) {
                throw new IllegalArgumentException(
                        "Could not instantiate Collection type: " + targetType.getName(), ex);
            }
        }
    }

    enum Sex {
        f;
    }

    private static Class<? extends Enum> asEnumType(Class<?> enumType) {
        Objects.requireNonNull(enumType, "Enum type must not be null");
        if (!Enum.class.isAssignableFrom(enumType)) {
            throw new IllegalArgumentException("Supplied type is not an enum: " + enumType.getName());
        }

        // 类型窄化
        return enumType.asSubclass(Enum.class);
    }

    public static <K, V> MultiValueMap<K, V> toMultiValueMap(Map<K, List<V>> targetMap) {
        // Assert.notNull(targetMap, "'targetMap' must not be null");
        return new MultiValueMapAdapter<>(targetMap);
    }
}
