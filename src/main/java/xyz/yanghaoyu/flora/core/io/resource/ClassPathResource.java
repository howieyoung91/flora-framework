package xyz.yanghaoyu.flora.core.io.resource;

import xyz.yanghaoyu.flora.core.io.Resource;
import xyz.yanghaoyu.flora.util.ReflectUtil;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ClassPathResource implements Resource {
    private final String path;

    private ClassLoader classLoader;

    public ClassPathResource(String path) {
        this(path, null);
    }

    public ClassPathResource(String path, ClassLoader classLoader) {
        if (path == null) {
            throw new NullPointerException("path must not be null!");
        }
        this.path = path;
        this.classLoader = (classLoader != null ? classLoader : ReflectUtil.getDefaultClassLoader());
    }

    @Override
    public InputStream getInputStream() throws IOException {
        InputStream is = classLoader.getResourceAsStream(path);
        if (is == null) {
            throw new FileNotFoundException(this.path + " cannot be opened because it does not exist");
        }
        return is;
    }
}
