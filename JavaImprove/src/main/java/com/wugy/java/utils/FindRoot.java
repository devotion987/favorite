package com.wugy.java.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * 查找系统的根路径，需要注意的是，由于RA有两种形态，<br>
 * 一种是独立应用，一种是WAR形式部署于其它中间件的<br>
 * 对于第一种情况，使用context.xml作为判断依据<br>
 * 对于第二种情况，使用web.xml作为判断依据
 */
public class FindRoot {

    private FindRoot(){
    }

    private static Class<?> claz = FindRoot.class;
    /**
     * 作为独立应用启动时的应用根路径
     */
    private static String AppRoot;

    /**
     * WebContent的根路径
     */
    private static String WebRoot;

    public static String getAppRoot() throws IOException, FileNotFoundException {

        if (AppRoot == null) {
            // 先找在$REALPATH/WebContent/WEB-INF/lib目录中jar包的情况
            String path = Path.getRootRelativePath("../../", claz);
            File context = new File(path + "/conf/context.xml");
            if (!context.exists()) {
                // 再找在$REALPATH/WebContent/WEB-INF/classes目录的情况
                path = Path.getRootRelativePath("../../../", claz);
                context = new File(path + "/conf/context.xml");
                if (!context.exists()) {
                    // 再找在$REALPATH/classes目录或$REALPATH/bin中的情况
                    path = Path.getRootRelativePath("../", claz);
                    context = new File(path + "/conf/context.xml");
                    if (!context.exists()) {
                        if (!context.exists()) {
                            // 再找在$REALPATH/lib目录中的情况
                            path = Path.getRootRelativePath("./", claz);
                            context = new File(path + "/conf/context.xml");
                            if (!context.exists()) {
                                System.out.println(context);
                                throw new IOException("程序被破坏或位置不正确，不能找到应用根路径");
                            }

                        }
                    }
                }
            }
            AppRoot = path;
        }
        return AppRoot;
    }

    public static String getWebRoot() throws IOException, FileNotFoundException {

        if (WebRoot == null) {
            if (getAppRoot() != null) {// 独立应用
                WebRoot = FindRoot.getAppRoot() + File.separatorChar
                        + "WebContent";
            } else {// WAR应用
                // 先找在$REALPATH/WebContent/WEB-INF/lib目录中jar包的情况
                String path = Path.getRootRelativePath("../", claz);
                File context = new File(path + "/WEB-INF/web.xml");
                if (!context.exists()) {
                    // 再找在$REALPATH/WebContent/WEB-INF/classes目录的情况
                    path = Path.getRootRelativePath("../../", claz);
                    context = new File(path + "/WEB-INF/web.xml");
                    if (!context.exists()) {
                        System.out.println(context);
                        throw new IOException("程序被破坏或位置不正确，不能找到应用根路径");
                    }
                }
                WebRoot = path;
            }
        }
        return WebRoot;

    }

    /**
     * 设置根路径的查找类
     *
     * @param claz
     */
    public static void setClaz(Class<?> claz) {
        FindRoot.claz = claz;
    }
}
