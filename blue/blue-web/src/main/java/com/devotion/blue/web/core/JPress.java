package com.devotion.blue.web.core;

import com.devotion.blue.web.core.render.freemarker.JFunction;
import com.devotion.blue.web.core.render.freemarker.JTag;
import com.jfinal.core.JFinal;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.render.FreeMarkerRender;

import java.util.HashMap;
import java.util.Map;

public class JPress {

    public static final String VERSION = "0.5.0";

    public static final Map<String, Object> jPressTags = new HashMap<>();

    public static void addTag(String key, JTag tag) {
        if (key.startsWith("jp.")) {
            key = key.substring(3);
        }
        jPressTags.put(key, tag);
    }

    public static void addFunction(String key, JFunction function) {
        FreeMarkerRender.getConfiguration().setSharedVariable(key, function);
    }

    public static void renderImmediately() {
        FreeMarkerRender.getConfiguration().setTemplateUpdateDelayMilliseconds(0);
    }

    private static boolean isInstalled = false;

    public static boolean isInstalled() {
        if (!isInstalled) {
//            File dbConfig = new File(PathKit.getRootClassPath(), JPressConfig.configFile);
//            isInstalled = dbConfig.exists();
            Prop prop = PropKit.use(JPressConfig.configFile);
            isInstalled = prop.getBoolean("isInstalled", false);
        }
        return isInstalled;
    }

    public static boolean isDevMode() {
        return JFinal.me().getConstants().getDevMode();
    }

    private static boolean isLoaded = false;

    public static boolean isLoaded() {
        return isLoaded;
    }

    public static void loadFinished() {
        isLoaded = true;
    }

}
