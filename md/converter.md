# Converter 类型转换

Flora 已经内置了类型转换服务,它依赖于 `Converter`
目前已经内置了以下 `Converter`

1. `ArrayToArrayConverter`
2. `ArrayToCollectionConverter`
3. `ArrayToStringConverter`
4. `CharacterToNumberFactory`
5. `CollectionToArrayConverter`
6. `CollectionToCollectionConverter`
7. `CollectionToStringConverter`
8. `EnumToStringConverter`
9. `MapToMapConverter`
10. `NumberToCharacterConverter`
11. `NumberToNumberConverterFactory`
12. `ObjectToStringConverter`
13. `PropertiesToStringConverter`
14. `StringToArrayConverter`
15. `StringToBooleanConverter`
16. `StringToCharacterConverter`
17. `StringToCollectionConverter`
18. `StringToEnumConverterFactory`
19. `StringToNumberConverterFactory`
20. `StringToPropertiesConverter`

在某些情况下 类型转换会自动发生

如以下代码:

```properties
# token.properties
username=howieyoung
age=18
createDate=2021-12-11
```

```java
public class User {
    // 注入成功 类型一致不需要转换
    @Value("${username}")
    String username;

    // 注入成功 使用内置转换器 StringToNumberConverterFactory 
    // 把 String 转为 Integer
    @Value("${age}")
    Integer age;

    // 注入失败, 类型不匹配, 同时找不到 String -> LocalDate 的转换器
    @Value("${createDate}")
    LocalDate createDate;
}
```

由于 StringToLocalDateConverter 并不存在

你可以手写一个 `Converter` 使其支持 String -> LocalDate 的转换

```java

@Component
public class StringToLocalDateConverter implements Converter<String, LocalDate> {
    private final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public LocalDate convert(String source) {
        return LocalDate.parse(source, DATE_TIME_FORMATTER);
    }
}
```

编写类 `StringToLocalDateConverter` 实现 Converter<S,T>接口  
`StringToLocalDateConverter`会被自动地导入类型转换服务

当需要把 `String` 转为 `LocalDate` 时 就会调用 `StringToLocalDateConverter#converter`
这样就完成了`String`转为`LocalDate`的支持
