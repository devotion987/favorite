package com.devotion.blue.model.core;

import java.util.HashMap;
import java.util.Map;

public class JModelMapping {

    protected static Map<String, String> tableMapping = new HashMap<>();

    public void mapping(String key, String value) {
        tableMapping.put(key, value);
    }

    private static JModelMapping me = new JModelMapping();

    public static JModelMapping me() {
        return me;
    }

    private JModelMapping() {
    }

    public String tx(String sql) {
        for (Map.Entry<String, String> entry : tableMapping.entrySet()) {
            sql = sql.replace(" " + entry.getKey() + " ", String.format(" `%s` ", entry.getValue()));
            sql = sql.replace(" " + entry.getKey() + ",", String.format(" `%s`,", entry.getValue()));
            sql = sql.replace(" " + entry.getKey() + ".", String.format(" `%s`.", entry.getValue()));
            sql = sql.replace("," + entry.getKey() + " ", String.format(",`%s` ", entry.getValue()));
            sql = sql.replace("," + entry.getKey() + ".", String.format(",`%s`.", entry.getValue()));
            // sql = sql.replace(entry.getKey() + "`", entry.getValue() + "`");
        }
        return sql;
    }

}
