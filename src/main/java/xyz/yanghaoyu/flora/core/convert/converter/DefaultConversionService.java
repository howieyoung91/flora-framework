package xyz.yanghaoyu.flora.core.convert.converter;

import xyz.yanghaoyu.flora.core.convert.converter.support.*;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/11/21 17:37<i/>
 * @version 1.0
 */


public class DefaultConversionService extends GenericConversionService {

    public DefaultConversionService() {
        addDefaultConverters(this);
    }

    public static void addDefaultConverters(ConverterRegistry converterRegistry) {

        converterRegistry.addConverterFactory(new StringToNumberConverterFactory());
        converterRegistry.addConverterFactory(new CharacterToNumberFactory());
        converterRegistry.addConverterFactory(new StringToEnumConverterFactory());
        converterRegistry.addConverterFactory(new NumberToNumberConverterFactory());

        converterRegistry.addConverter(new EnumToStringConverter());
        converterRegistry.addConverter(new NumberToCharacterConverter());
        converterRegistry.addConverter(new ObjectToStringConverter());
        converterRegistry.addConverter(new StringToBooleanConverter());
        converterRegistry.addConverter(new StringToCharacterConverter());

    }
}
