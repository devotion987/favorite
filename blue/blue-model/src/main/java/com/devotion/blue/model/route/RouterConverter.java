package com.devotion.blue.model.route;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.devotion.blue.model.query.OptionQuery;
import com.devotion.blue.utils.StringUtils;
import com.jfinal.core.JFinal;

public abstract class RouterConverter {

    public static final String URL_PARA_SEPARATOR = JFinal.me().getConstants().getUrlParaSeparator();
    public static final String SLASH = "/";

    public abstract String converter(String target, HttpServletRequest request, HttpServletResponse response);

    public static BigInteger tryGetBigInteger(String param) {
        try {
            return new BigInteger(param);
        } catch (Exception e) {
        }
        return null;
    }

    public static String[] parseTarget(String target) {
        String[] strings = target.split(SLASH);
        List<String> arrays = new ArrayList<>();
        for (String string : strings) {
            if (StringUtils.isNotBlank(string)) {
                arrays.add(string);
            }
        }
        return arrays.toArray(new String[]{});
    }

    public static String[] parseParam(String param) {
        String[] strings = param.split(URL_PARA_SEPARATOR);
        List<String> arrays = new ArrayList<>();
        for (String string : strings) {
            if (StringUtils.isNotBlank(string)) {
                arrays.add(string);
            }
        }
        return arrays.toArray(new String[]{});
    }

    protected static boolean enalbleFakeStatic() {
        Boolean fakeStaticEnable = OptionQuery.me().findValueAsBool("router_fakestatic_enable");
        return fakeStaticEnable != null && fakeStaticEnable == true;
    }

    protected static String getFakeStaticSuffix() {
        String fakeStaticSuffix = OptionQuery.me().findValue("router_fakestatic_suffix");
        if (StringUtils.isNotBlank(fakeStaticSuffix)) {
            return fakeStaticSuffix.trim();
        }
        return ".html";
    }

}
