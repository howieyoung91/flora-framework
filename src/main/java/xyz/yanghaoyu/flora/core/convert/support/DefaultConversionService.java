package xyz.yanghaoyu.flora.core.convert.support;

import xyz.yanghaoyu.flora.core.convert.converter.ConversionService;
import xyz.yanghaoyu.flora.core.convert.converter.ConverterRegistry;
import xyz.yanghaoyu.flora.core.convert.converter.GenericConversionService;

import java.util.*;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/11/21 17:37<i/>
 * @version 1.0
 */

public class DefaultConversionService extends GenericConversionService {

    public DefaultConversionService() {
        addDefaultConverters(this);
    }

    public static void main(String[] args) {
        Object[] temp = {5484.0, 25.0, true, false};
        ArrayList<Object> ls = new ArrayList<Object>() {{
            add(1);
            add(1.2);
            add(1556.5);
            add(true);
            add(false);
        }};

        Set<Object> set = new HashSet<>(ls);
        DefaultConversionService service = new DefaultConversionService();
        HashMap<String, Boolean> src = new HashMap<>();
        src.put("true", true);
        src.put("false", false);
        Map convert = service.convert(src, LinkedHashMap.class);
        System.out.println(convert);
    }

    public static void addDefaultConverters(ConverterRegistry converterRegistry) {
        addScalarConverters(converterRegistry);
        addCollectionConverters(converterRegistry);
    }

    private static void addCollectionConverters(ConverterRegistry converterRegistry) {
        ConversionService service = (ConversionService) converterRegistry;

        converterRegistry.addConverter(new StringToArrayConverter(service));
        converterRegistry.addConverter(new ArrayToStringConverter(service));

        converterRegistry.addConverter(new CollectionToStringConverter(service));
        converterRegistry.addConverter(new StringToCollectionConverter(service));

        converterRegistry.addConverter(new CollectionToArrayConverter(service));
        converterRegistry.addConverter(new ArrayToCollectionConverter());

        converterRegistry.addConverter(new ArrayToArrayConverter(service));

        converterRegistry.addConverter(new CollectionToCollectionConverter());

        // fixme
        converterRegistry.addConverter(new MapToMapConverter());
    }

    private static void addScalarConverters(ConverterRegistry converterRegistry) {

        converterRegistry.addConverterFactory(new NumberToNumberConverterFactory());

        converterRegistry.addConverter(new EnumToStringConverter());
        converterRegistry.addConverterFactory(new StringToEnumConverterFactory());

        converterRegistry.addConverter(new NumberToCharacterConverter());
        converterRegistry.addConverterFactory(new CharacterToNumberFactory());


        converterRegistry.addConverterFactory(new StringToNumberConverterFactory());
        converterRegistry.addConverter(Number.class, String.class, new ObjectToStringConverter());

        converterRegistry.addConverter(new StringToBooleanConverter());
        converterRegistry.addConverter(Boolean.class, String.class, new ObjectToStringConverter());

        converterRegistry.addConverter(new StringToCharacterConverter());
        converterRegistry.addConverter(Character.class, String.class, new ObjectToStringConverter());

        converterRegistry.addConverter(new StringToPropertiesConverter());
        converterRegistry.addConverter(new PropertiesToStringConverter());

        converterRegistry.addConverter(new ObjectToStringConverter());
    }
}
