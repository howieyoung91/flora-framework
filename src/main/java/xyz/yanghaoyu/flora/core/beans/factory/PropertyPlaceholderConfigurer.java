package xyz.yanghaoyu.flora.core.beans.factory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import xyz.yanghaoyu.flora.core.beans.factory.config.BeanDefinition;
import xyz.yanghaoyu.flora.core.beans.factory.config.BeanFactoryPostProcessor;
import xyz.yanghaoyu.flora.core.io.Resource;
import xyz.yanghaoyu.flora.core.io.loader.DefaultResourceLoader;
import xyz.yanghaoyu.flora.exception.BeansException;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class PropertyPlaceholderConfigurer implements BeanFactoryPostProcessor {

    /**
     * Default placeholder prefix: {@value}
     */
    public static final String DEFAULT_PLACEHOLDER_PREFIX = "${";

    /**
     * Default placeholder suffix: {@value}
     */
    public static final String DEFAULT_PLACEHOLDER_SUFFIX = "}";
    public static final String LOCATIONS = "locations";
    private Set<String> locations;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        try {
            // 加载属性文件
            DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
            Map<String, String> properties = loadProperties(resourceLoader);

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
            throw new BeansException("Could not load properties", e);
        }
    }

    private Map<String, String> loadProperties(DefaultResourceLoader resourceLoader) throws IOException {
        Map<String, String> map = new HashMap<>(8);
        Properties properties = new Properties();
        ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());
        for (String location : locations) {
            Resource resource = resourceLoader.getResource(location);
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
        map.putAll((Map) properties);
        return map;
    }

    public Map<String, String> flatTree(Map<String, Object> tree) {
        HashMap<String, String> res = new HashMap<>();
        tree.forEach((k, v) -> {
            if (v instanceof Map) {
                flatTree((Map<String, Object>) v)
                        .forEach((s, s2) -> res.put(k + "." + s, s2));
            } else {
                res.put(k, v.toString());
            }
        });
        return res;
    }

    private String resolvePlaceholder(String value, Map<String, String> properties) {
        StringBuilder builder = new StringBuilder(value);
        int startIdx = value.indexOf(DEFAULT_PLACEHOLDER_PREFIX);
        int stopIdx = value.indexOf(DEFAULT_PLACEHOLDER_SUFFIX);
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

