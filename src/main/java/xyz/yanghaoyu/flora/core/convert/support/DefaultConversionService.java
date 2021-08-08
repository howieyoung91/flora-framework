package xyz.yanghaoyu.flora.core.convert.support;

import xyz.yanghaoyu.flora.core.convert.converter.ConversionService;
import xyz.yanghaoyu.flora.core.convert.converter.ConverterRegistry;
import xyz.yanghaoyu.flora.core.convert.converter.GenericConversionService;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/11/21 17:37<i/>
 * @version 1.0
 */

public class DefaultConversionService extends GenericConversionService {

    public DefaultConversionService() {
        addDefaultConverters(this);
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
