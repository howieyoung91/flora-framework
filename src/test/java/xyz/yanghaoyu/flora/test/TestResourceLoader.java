package xyz.yanghaoyu.flora.test;

import cn.hutool.core.io.IoUtil;
import org.junit.Before;
import org.junit.Test;
import xyz.yanghaoyu.flora.core.io.loader.DefaultResourceLoader;
import xyz.yanghaoyu.flora.core.io.resource.Resource;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author <a href="https://www.yanghaoyu.xyz">Howie Young</a><i>on 2021/8/8 11:58<i/>
 * @version 1.0
 */


public class TestResourceLoader {
    private DefaultResourceLoader resourceLoader;

    @Before
    public void init() {
        resourceLoader = new DefaultResourceLoader();
    }

    @Test
    public void test() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:application.xml");
        InputStream inputStream = resource.getInputStream();
        String content = IoUtil.readUtf8(inputStream);
        System.out.println(content);
    }
}
