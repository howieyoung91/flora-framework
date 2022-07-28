/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.framework.core.context.annotation;

import xyz.yanghaoyu.flora.framework.util.ReflectUtil;
import xyz.yanghaoyu.flora.framework.util.StringUtil;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author <a href="https://www.yanghaoyu.xyz">Howie Young</a><i>on 2021/3/13 15:14<i/>
 * @version 1.0
 */

public abstract class ClassScanner {
    protected final String        targetPackage;
    protected final Set<Class<?>> classSet = new HashSet<>();

    public ClassScanner(String pkgName) {
        this.targetPackage = pkgName;
    }

    public Set<Class<?>> getClassSet() {
        return classSet;
    }

    public final ClassScanner scan() {
        try {
            // 从包名获取 URL 类型的资源
            Enumeration<URL> urls = ReflectUtil.getDefaultClassLoader().getResources(targetPackage.replace(".", "/"));
            // 遍历 URL 资源
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                if (url != null) {
                    String protocol = url.getProtocol(); // 获取协议名（分为 file 与 jar）
                    if (protocol.equals("file")) {
                        resolvePackages(url);
                    }
                    else if (protocol.equals("jar")) {
                        resolveJar(url);
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    private void resolvePackages(URL url) {
        // 若在 class 目录中，则执行添加类操作
        String packagePath = url.getPath().replaceAll("%20", " ");
        addPackage(packagePath, targetPackage);
    }

    private void resolveJar(URL url) throws IOException {
        // 若在 jar 包中，则解析 jar 包中的 entry
        JarURLConnection      jarURLConnection = (JarURLConnection) url.openConnection();
        JarFile               jarFile          = jarURLConnection.getJarFile();
        Enumeration<JarEntry> jarEntries       = jarFile.entries();
        while (jarEntries.hasMoreElements()) {
            JarEntry jarEntry     = jarEntries.nextElement();
            String   jarEntryName = jarEntry.getName();
            // 判断该 entry 是否为 class
            if (jarEntryName.endsWith(".class")) {
                // 获取类名
                String className = jarEntryName.substring(0, jarEntryName.lastIndexOf(".")).replaceAll("/", ".");
                // 执行添加类操作
                addClass(className);
            }
        }
        jarFile.close();
    }

    private void addPackage(String packagePath, String packageName) {
        try {
            // 获取包名路径下的 class 文件或目录
            File[] files = new File(packagePath).listFiles(file -> (file.isFile() && file.getName().endsWith(".class")) || file.isDirectory());
            // 遍历文件或目录
            for (File file : files) {
                String fileName = file.getName();
                // 判断是否为文件或目录
                if (file.isFile()) {
                    // 获取类名
                    String className = fileName.substring(0, fileName.lastIndexOf("."));
                    if (StringUtil.isNotEmpty(packageName)) {
                        className = packageName + "." + className;
                    }
                    // 执行添加类操作
                    addClass(className);
                }
                else {
                    // 获取子包
                    String subPackagePath = fileName;
                    if (StringUtil.isNotEmpty(packagePath)) {
                        subPackagePath = packagePath + "/" + subPackagePath;
                    }
                    // 子包名
                    String subPackageName = fileName;
                    if (StringUtil.isNotEmpty(packageName)) {
                        subPackageName = packageName + "." + subPackageName;
                    }
                    // 递归调用
                    addPackage(subPackagePath, subPackageName);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addClass(String className) {
        // 加载类
        Class<?> clazz = ReflectUtil.loadClass(className, false);
        // 判断是否可以添加类
        doAddClass(clazz);
    }

    private void doAddClass(Class<?> clazz) {
        if (canAdd(clazz)) {
            classSet.add(clazz);
        }
        // add inner classes
        Class<?>[] innerClasses = clazz.getDeclaredClasses();
        for (Class<?> innerClass : innerClasses) {
            doAddClass(innerClass);
        }
    }

    /**
     * 验证是否允许添加类
     */
    public abstract boolean canAdd(Class<?> cls);

}
