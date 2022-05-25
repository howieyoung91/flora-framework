package xyz.yanghaoyu.flora.core.beans.factory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import xyz.yanghaoyu.flora.core.Ordered;
import xyz.yanghaoyu.flora.core.beans.factory.config.BeanDefinition;
import xyz.yanghaoyu.flora.core.beans.factory.config.BeanFactoryPostProcessor;
import xyz.yanghaoyu.flora.core.beans.factory.support.InitializingBean;
import xyz.yanghaoyu.flora.core.io.Resource;
import xyz.yanghaoyu.flora.core.io.loader.DefaultResourceLoader;
import xyz.yanghaoyu.flora.exception.BeansException;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * 处理 property，用于 @Value 的支持
 * 这个执行优先级尽可能地高
 */
public class PropertyPlaceholderConfigurer
        implements BeanFactoryPostProcessor, InitializingBean, Ordered {
    public static final String DEFAULT_PLACEHOLDER_PREFIX = "${";
    public static final String DEFAULT_PLACEHOLDER_SUFFIX = "}";
    public static final String LOCATIONS                  = "locations";

    private       Set<String>        locations;
    private final LinkedList<String> usingLocations = new LinkedList<>();

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE / 2;
    }

    @Override
    public void afterPropertiesSet() {
        // 这里要处理了 locations 乱序的问题
        // 确保库的配置文件比项目配置文件先解析
        // todo 实验功能
        for (String location : locations) {
            usingLocations.addFirst(location);
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        try {
            // 加载属性文件
            DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
            Map<String, String>   properties     = loadProperties(resourceLoader);

            String[] beanDefinitionNames = beanFactory.getBeanDefinitionNames();
            for (String beanName : beanDefinitionNames) {
                BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);

                PropertyValues propertyValues = beanDefinition.getPropertyValues();
                // 对 XML 做支持
                for (PropertyValue propertyValue : propertyValues.getPropertyValues()) {
                    Object value = propertyValue.getValue();
                    // 必须是 String
                    if (!(value instanceof String)) {
                        continue;
                    }
                    // 把 ${value} 替换为真正的值
                    String tempValue = resolvePlaceholder((String) value, properties);
                    if (tempValue == null) {
                        throw new NullPointerException();
                    }
                    value = tempValue;
                    propertyValues.addPropertyValue(new PropertyValue(propertyValue.getName(), value));
                }
            }

            // 向容器中添加字符串解析器，供解析 @Value 注解使用
            StringValueResolver valueResolver =
                    new PlaceholderResolvingStringValueResolver(properties);
            beanFactory.addEmbeddedValueResolver(valueResolver);

        } catch (IOException e) {
            e.printStackTrace();
            throw new BeansException("Could not load properties", e);
        }
    }

    private Map<String, String> loadProperties(DefaultResourceLoader resourceLoader) throws IOException {
        Map<String, String> map        = new HashMap<>(8);
        Properties          properties = new Properties();
        ObjectMapper        yamlMapper = new ObjectMapper(new YAMLFactory());

        for (String location : usingLocations) {
            // System.out.println(location);
            Resource    resource    = resourceLoader.getResource(location);
            InputStream inputStream = resource.getInputStream();
            if (location.endsWith(".properties")) {
                properties.load(inputStream);
            } else if (location.endsWith(".yaml") || location.endsWith(".yml")) {
                Map yamlMap = flatTree(yamlMapper.readValue(inputStream, Map.class));
                map.putAll(yamlMap);
            } else {
                throw new RuntimeException("unsupported property file!");
            }
            inputStream.close();
        }
        // System.out.println(properties);
        map.putAll((Map) properties);
        return map;
    }

    public static Map<String, String> flatTree(Map<String, Object> tree) {
        HashMap<String, String> res = new HashMap<>();
        tree.forEach((k, v) -> {
            if (v instanceof Map) {
                flatTree((Map<String, Object>) v)
                        .forEach((s, s2) -> res.put(k + "." + s, s2));
            } else {
                String value = v.toString();
                if (v instanceof List) {
                    value = value.substring(1, value.length() - 1).trim();
                }
                res.put(k, value);
            }
        });
        return res;
    }

    private String resolvePlaceholder(String value, Map<String, String> properties) {
        StringBuilder builder  = new StringBuilder(value);
        int           startIdx = value.indexOf(DEFAULT_PLACEHOLDER_PREFIX);
        int           stopIdx  = value.indexOf(DEFAULT_PLACEHOLDER_SUFFIX);
        if (startIdx != -1 && stopIdx != -1 && startIdx < stopIdx) {
            String propKey = value.substring(startIdx + 2, stopIdx);
            String propVal = properties.get(propKey);
            if (propVal == null) {
                return null;
            }
            builder.replace(startIdx, stopIdx + 1, propVal);
        }
        return builder.toString();
    }


    // 对 @Value 做支持
    private class PlaceholderResolvingStringValueResolver implements StringValueResolver {

        private final Map<String, String> properties;

        public PlaceholderResolvingStringValueResolver(Map<String, String> properties) {
            this.properties = properties;
        }

        @Override
        public String resolveStringValue(String value) {
            return PropertyPlaceholderConfigurer.this.resolvePlaceholder(value, properties);
        }
    }


}

