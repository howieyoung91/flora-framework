package xyz.yanghaoyu.flora.core.io.loader;

import xyz.yanghaoyu.flora.core.io.ResourceLoader;
import xyz.yanghaoyu.flora.core.io.resource.ClassPathResource;
import xyz.yanghaoyu.flora.core.io.resource.FileSystemResource;
import xyz.yanghaoyu.flora.core.io.Resource;
import xyz.yanghaoyu.flora.core.io.resource.UrlResource;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/8/8 10:52<i/>
 * @version 1.0
 */

public class DefaultResourceLoader implements ResourceLoader {
    @Override
    public Resource getResource(String location) {
        if (location == null) {
            throw new NullPointerException("location must not be null!");
        }
        if (location.startsWith(CLASSPATH_URL_PREFIX)) {
            return new ClassPathResource(location.substring(CLASSPATH_URL_PREFIX.length()));
        } else {
            try {
                URL url = new URL(location);
                return new UrlResource(url);
            } catch (MalformedURLException e) {
                return new FileSystemResource(location);
            }
        }
    }
}

