/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.framework.core.io.spi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import xyz.yanghaoyu.flora.framework.core.beans.factory.PropertyPlaceholderConfigurer;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public final class FloraFactoriesLoader {
    public static final String YAML_FACTORIES_RESOURCE_LOCATION = "META-INF/flora.yaml";

    private FloraFactoriesLoader() {
        throw new UnsupportedOperationException();
    }

    private static final Map<ClassLoader, Map<String, Map<String, String>>> cache = new HashMap<>();

    public static Map<String, Map<String, String>> loadFactories() {
        return loadFactories(null);
    }

    public static Map<String, Map<String, String>> loadFactories(ClassLoader classLoader) {
        if (classLoader == null) {
            classLoader = Thread.currentThread().getContextClassLoader();
        }
        Map<String, Map<String, String>> map = cache.get(classLoader);
        if (map != null) {
            return map;
        }
        map = new HashMap<>();
        cache.put(classLoader, map);
        try {
            Enumeration<URL> resources = classLoader.getResources(YAML_FACTORIES_RESOURCE_LOCATION);
            while (resources.hasMoreElements()) {
                URL                 url         = resources.nextElement();
                InputStream         inputStream = url.openStream();
                ObjectMapper        yamlMapper  = new ObjectMapper(new YAMLFactory());
                Map<String, String> yamlMap     = PropertyPlaceholderConfigurer.flatTree(yamlMapper.readValue(inputStream, Map.class));
                map.put(url.getPath(), yamlMap);
                inputStream.close();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

}
