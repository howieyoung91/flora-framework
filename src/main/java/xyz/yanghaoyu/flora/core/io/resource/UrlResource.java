package xyz.yanghaoyu.flora.core.io.resource;

import xyz.yanghaoyu.flora.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;


public class UrlResource implements Resource {
    private final URL url;

    public UrlResource(URL url) {
        if (url == null) {
            throw new NullPointerException("url must not be null!");
        }
        this.url = url;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        URLConnection con = this.url.openConnection();
        try {
            return con.getInputStream();
        }
        catch (IOException ex) {
            if (con instanceof HttpURLConnection) {
                ((HttpURLConnection) con).disconnect();
            }
            throw ex;
        }
    }
}
