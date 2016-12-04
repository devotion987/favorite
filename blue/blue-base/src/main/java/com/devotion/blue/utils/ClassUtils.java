package com.devotion.blue.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.jfinal.kit.PathKit;
import com.jfinal.log.Log;

public class ClassUtils {

    private static final Log log = Log.getLog(ClassUtils.class);

    public static <T> List<Class<T>> scanSubClass(Class<T> pClazz) {
        return scanSubClass(pClazz, false);
    }

    public static <T> List<Class<T>> scanSubClass(Class<T> pClazz, boolean canNewInstance) {
        if (pClazz == null) {
            log.error("scanClass: parent clazz is null");
            return null;
        }

        List<File> classFileList = new ArrayList<>();
        scanClass(classFileList, PathKit.getRootClassPath());

        List<Class<T>> classList = new ArrayList<>();
        for (File file : classFileList) {

            int start = PathKit.getRootClassPath().length();
            int end = file.toString().length() - 6; // 6 == ".class".length();

            String classFile = file.toString().substring(start + 1, end);
            Class<T> clazz = classForName(classFile.replace(File.separator, "."));

            addClassList(pClazz, canNewInstance, classList, clazz);
        }

        File jarsDir = new File(PathKit.getWebRootPath() + "/WEB-INF/lib");
        if (jarsDir.exists() && jarsDir.isDirectory()) {
            FileFilter filter = (pathname) -> {
                String name = pathname.getName().toLowerCase();
                return name.endsWith(".jar") && name.startsWith("blue");
            };
            File[] jarFiles = jarsDir.listFiles(filter);

            if (jarFiles != null && jarFiles.length > 0) {
                for (File f : jarFiles) {
                    classList.addAll(scanSubClass(pClazz, f, canNewInstance));
                }
            }
        }
        return classList;
    }

    private static <T> void addClassList(Class<T> pClazz, boolean canNewInstance, List<Class<T>> classList, Class<T> clazz) {
        if (clazz != null && pClazz.isAssignableFrom(clazz)) {
            if (canNewInstance) {
                if (clazz.isInterface())
                    return;

                if (Modifier.isAbstract(clazz.getModifiers()))
                    return;
            }
            classList.add(clazz);
        }
    }

    public static <T> List<Class<T>> scanSubClass(Class<T> pClazz, File f, boolean canNewInstance) {
        if (pClazz == null) {
            log.error("scanClass: parent clazz is null");
            return null;
        }
        JarFile jarFile = null;
        try {
            jarFile = new JarFile(f);
            List<Class<T>> classList = new ArrayList<>();
            Enumeration<JarEntry> entries = jarFile.entries();

            while (entries.hasMoreElements()) {
                JarEntry jarEntry = entries.nextElement();
                String entryName = jarEntry.getName();
                if (!jarEntry.isDirectory() && entryName.endsWith(".class")) {
                    String className = entryName.replace("/", ".").substring(0, entryName.length() - 6);
                    Class<T> clazz = classForName(className);
                    addClassList(pClazz, canNewInstance, classList, clazz);
                }
            }
            return classList;
        } catch (IOException e1) {
            log.error("read jarFile error:" + e1);
        } finally {
            if (jarFile != null)
                try {
                    jarFile.close();
                } catch (IOException e) {
                    log.error("jarFile close error:" + e);
                }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private static <T> Class<T> classForName(String className) {
        Class<T> clazz = null;
        try {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            clazz = (Class<T>) Class.forName(className, false, cl);
        } catch (Throwable e) {
            log.error("classForName is error，className:" + className);
        }
        return clazz;
    }

    private static void scanClass(List<File> fileList, String path) {
        File files[] = new File(path).listFiles();
        if (null == files || files.length == 0)
            return;
        for (File file : files) {
            if (file.isDirectory()) {
                scanClass(fileList, file.getAbsolutePath());
            } else if (file.getName().endsWith(".class")) {
                fileList.add(file);
            }
        }
    }

}
