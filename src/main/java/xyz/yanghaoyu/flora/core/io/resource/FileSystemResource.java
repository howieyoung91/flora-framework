package xyz.yanghaoyu.flora.core.io.resource;

import xyz.yanghaoyu.flora.core.io.Resource;

import java.io.*;

public class FileSystemResource implements Resource {
    private final File file;
    private final String path;

    public FileSystemResource(File file) {
        this.file = file;
        this.path = file.getPath();
    }

    public FileSystemResource(String path) {
        this.file = new File(path);
        this.path = path;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new BufferedInputStream(new FileInputStream(this.file));
    }

    public final String getPath() {
        return this.path;
    }
}
