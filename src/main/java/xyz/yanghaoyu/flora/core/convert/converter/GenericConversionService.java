package xyz.yanghaoyu.flora.core.convert.converter;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * 泛型转换服务
 *
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/11/21 17:02<i/>
 * @version 1.0
 */

public class GenericConversionService implements ConversionService, ConverterRegistry {
    private Map<GenericConverter.ConvertiblePair, GenericConverter> converters = new HashMap<>();

    @Override
    public boolean canConvert(Class<?> sourceType, Class<?> targetType) {
        return getConverter(sourceType, targetType) != null;
    }

    @Override
    public <T> T convert(Object source, Class<T> targetType) {
        Class<?> sourceType = source.getClass();
        GenericConverter converter = getConverter(sourceType, targetType);
        if (converter == null) {
            throw new RuntimeException("find no converter to convert " + sourceType + " to " + targetType);
        }
        return (T) converter.convert(source, sourceType, targetType);
    }


    @Override
    public void addConverter(Converter<?, ?> converter) {
        GenericConverter.ConvertiblePair typeInfo = getRequiredTypeInfo(converter);
        ConverterAdapter converterAdapter = new ConverterAdapter(typeInfo, converter);
        for (GenericConverter.ConvertiblePair convertibleType : converterAdapter.getConvertibleTypes()) {
            converters.put(convertibleType, converterAdapter);
        }
    }

    @Override
    public void addConverter(GenericConverter converter) {
        for (GenericConverter.ConvertiblePair convertibleType : converter.getConvertibleTypes()) {
            converters.put(convertibleType, converter);
        }
    }

    @Override
    public void addConverterFactory(ConverterFactory<?, ?> converterFactory) {
        GenericConverter.ConvertiblePair typeInfo = getRequiredTypeInfo(converterFactory);
        ConverterFactoryAdapter converterFactoryAdapter = new ConverterFactoryAdapter(typeInfo, converterFactory);
        for (GenericConverter.ConvertiblePair convertibleType : converterFactoryAdapter.getConvertibleTypes()) {
            converters.put(convertibleType, converterFactoryAdapter);
        }
    }

    @Override
    public <S, T> void addConverter(Class<S> sourceType, Class<T> targetType, Converter<? super S, ? extends T> converter) {
        ConverterAdapter converterAdapter = new ConverterAdapter(
                new GenericConverter.ConvertiblePair(sourceType, targetType),
                converter
        );
        for (GenericConverter.ConvertiblePair convertibleType : converterAdapter.getConvertibleTypes()) {
            converters.put(convertibleType, converterAdapter);
        }
    }

    protected GenericConverter getConverter(Class<?> sourceType, Class<?> targetType) {
        Set<Class<?>> sourceCandidates = getClassHierarchy(sourceType);
        Set<Class<?>> targetCandidates = getClassHierarchy(targetType);
        for (Class<?> sourceCandidate : sourceCandidates) {
            for (Class<?> targetCandidate : targetCandidates) {
                GenericConverter.ConvertiblePair convertiblePair = new GenericConverter.ConvertiblePair(sourceCandidate, targetCandidate);
                GenericConverter converter = converters.get(convertiblePair);
                if (converter != null) {
                    return converter;
                }
            }
        }
        return null;
    }

    public int dimensionOf(Class clazz) {
        int count = 0;
        while (clazz.isArray()) {
            count++;
            clazz = clazz.getComponentType();
        }
        return count;
    }

    private static final Set<Class<?>> set = new HashSet<>();

    static {
        set.add(Cloneable.class);
        set.add(Serializable.class);
        set.add(RandomAccess.class);
        set.add(Comparable.class);
        set.add(Object.class);
        set.add(null);
    }

    public List<Class<?>> getClassHierarchyFromInterfaceListable(Class<?> clazz) {
        LinkedList<Class<?>> hierarchy = new LinkedList<>();
        if (clazz == null) {
            return hierarchy;
        }

        hierarchy.add(clazz);


        for (int i = 0; i < hierarchy.size(); i++) {
            Class<?> aClass = hierarchy.get(i);
            Class<?>[] interfaces = aClass.getInterfaces();
            for (Class<?> aninterface : interfaces) {
                if (set.contains(aninterface)) {
                    continue;
                }
                hierarchy.addAll(getClassHierarchyFromInterface(aninterface));
            }
        }

        return hierarchy;
    }

    public Set<Class<?>> getClassHierarchyFromInterface(Class<?> clazz) {
        Set<Class<?>> hierarchy = new LinkedHashSet<>();
        if (clazz == null) {
            return hierarchy;
        }

        hierarchy.add(clazz);
        Class<?>[] interfaces = clazz.getInterfaces();
        for (Class<?> aninterface : interfaces) {
            if (set.contains(aninterface)) {
                continue;
            }
            hierarchy.addAll(getClassHierarchyFromInterface(aninterface));
        }
        return hierarchy;
    }

    public Set<Class<?>> getClassHierarchyFromClass(Class<?> clazz) {
        // ArrayList
        Set<Class<?>> hierarchy = new LinkedHashSet<>();
        if (clazz == null) {
            return hierarchy;
        }
        hierarchy.add(clazz);

        Class<?> superClass = clazz.getSuperclass();
        if (!set.contains(superClass)) {
            Set<Class<?>> superClassSet = getClassHierarchyFromClass(superClass);
            hierarchy.addAll(superClassSet);
        }

        Class<?>[] interfaces = clazz.getInterfaces();
        for (Class<?> anInterface : interfaces) {
            if (set.contains(anInterface)) {
                continue;
            }
            hierarchy.addAll(getClassHierarchyFromInterface(anInterface));
        }
        return hierarchy;

    }

    public Set<Class<?>> getClassHierarchy(Class<?> clazz) {
        int dimension = dimensionOf(clazz);
        Set<Class<?>> hierarchy = new LinkedHashSet<>();

        // 数组 -> Integer[] Object[] Object;
        // Integer[][] Object[][] Object;

        if (dimension != 0) {
            int[] objects = new int[dimension];
            hierarchy.add(clazz);
            hierarchy.add(Array.newInstance(Object.class, objects).getClass());
            hierarchy.add(Object.class);
            return hierarchy;
        }

        // hierarchy = getClassHierarchyFromClass(clazz);

        while (clazz != Object.class) {
            hierarchy.add(clazz);
            Class<?> superclass = clazz.getSuperclass();

            Class<?> next = null;

            if (!set.contains(superclass)) {
                hierarchy.add(superclass);
                next = superclass;

                Class<?>[] interfaces = clazz.getInterfaces();
                for (Class<?> anInterface : interfaces) {
                    if (!set.contains(anInterface)) {
                        hierarchy.add(anInterface);
                    }
                }

            } else {
                Class<?>[] interfaces = clazz.getInterfaces();
                for (Class<?> anInterface : interfaces) {
                    if (!set.contains(anInterface)) {
                        hierarchy.addAll(getClassHierarchyFromInterfaceListable(anInterface));
                    }
                }
                break;
            }


            clazz = next;
        }


        hierarchy.add(Object.class);
        // System.out.println(hierarchy);
        return hierarchy;
    }

    // private void in(Class<?> aInterface) {
    //     Class<?>[] interfaces = aInterface.getInterfaces();
    //     for (Class anInterface : interfaces) {
    //
    //     }
    // }

    private GenericConverter.ConvertiblePair getRequiredTypeInfo(Object object) {
        // 获取泛型的参数
        Type[] types = object.getClass().getGenericInterfaces();
        ParameterizedType parameterized = (ParameterizedType) types[0];
        Type[] actualTypeArguments = parameterized.getActualTypeArguments();
        Class sourceType = (Class) actualTypeArguments[0];
        Class targetType = (Class) actualTypeArguments[1];

        return new GenericConverter.ConvertiblePair(sourceType, targetType);
    }

    // 把 Converter 适配成 GenericConverter
    private final class ConverterAdapter implements GenericConverter {

        private final ConvertiblePair typeInfo;

        private final Converter<Object, Object> converter;

        public ConverterAdapter(ConvertiblePair typeInfo, Converter<?, ?> converter) {
            this.typeInfo = typeInfo;
            this.converter = (Converter<Object, Object>) converter;
        }

        @Override
        public Set<ConvertiblePair> getConvertibleTypes() {
            return Collections.singleton(typeInfo);
        }

        @Override
        public Object convert(Object source, Class sourceType, Class targetType) {
            return converter.convert(source);
        }
    }

    // 把 ConverterFactory 适配成 GenericConverter
    private final class ConverterFactoryAdapter implements GenericConverter {

        private final ConvertiblePair typeInfo;

        private final ConverterFactory<Object, Object> converterFactory;

        public ConverterFactoryAdapter(ConvertiblePair typeInfo, ConverterFactory<?, ?> converterFactory) {
            this.typeInfo = typeInfo;
            this.converterFactory = (ConverterFactory<Object, Object>) converterFactory;
        }

        @Override
        public Set<ConvertiblePair> getConvertibleTypes() {
            return Collections.singleton(typeInfo);
        }

        @Override
        public Object convert(Object source, Class sourceType, Class targetType) {
            return converterFactory.getConverter(targetType).convert(source);
        }
    }
}
