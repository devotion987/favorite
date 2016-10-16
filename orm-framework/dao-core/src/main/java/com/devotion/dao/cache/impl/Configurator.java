/*
 * Copyright (C), 2015, IBM GBS China.
 * FileName: Configurator.java
 * Author:   www.ibm.com
 * Date:     
 * Description: //模块目的、功能描述
 * History:     //修改记录
 * <author>      <time>      <version>    <desc>
 * 修改人姓名             修改时间            版本号                  描述
 */
package com.devotion.dao.cache.impl;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.devotion.dao.cache.Cache;
import com.devotion.dao.cache.CacheConfig;
import com.devotion.dao.cache.CacheException;
import com.devotion.dao.cache.CacheFactory;

/**
 * 功能描述：缓存配置器
 */
public class Configurator {

    /**
     * 清除间隔
     */
    public static final int CLEAN_INTERVAL = 30000;

    /**
     * 秒
     */
    private static final long SECOND = 1000;

    /**
     * 分钟
     */
    private static final long MINUTE = SECOND * 60;

    /**
     * 小时
     */
    private static final long HOUR = MINUTE * 60;

    /**
     * KB
     */
    private static final long KB = 1024;

    /**
     * MB
     */
    private static final long MB = KB * 1024;

    /**
     * 装载缓存配置信息
     *
     * @param in 输入流
     */
    public static void loadConfig(InputStream in) {
        CacheFactory cf = CacheFactory.getInstance();
        try {
            /** 初始化DocumentBuilder */
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            /** 解析字节流生成文档输入流 */
            Document document = builder.parse(in);
            /** 获取文档子节点，生成节点list */
            NodeList nodeList = document.getChildNodes();
            Node node = nodeList == null || nodeList.getLength() == 0 ? null : nodeList.item(0);
            if (node == null || !"cache-factory".equalsIgnoreCase(node.getNodeName())) {
                throw new CacheException("root node must be \"cache-factory\"");
            }
            if (node instanceof Element) {
                long cleanInterval = getTimeLong(((Element) node).getAttribute("clean-interval"));
                if (cleanInterval > 0) {
                    cf.setCleanInterval(cleanInterval);
                }
            }
            /** 获取缓存各项配置信息并根据模板进行组装 */
            for (Node n = node.getFirstChild(); n != null; n = n.getNextSibling()) {
                if ((n instanceof Element) && "cache".equalsIgnoreCase(n.getNodeName())) {
                    Cache cache = new DefCacheImpl();

                    String id = ((Element) n).getAttribute("id");
                    String desc = ((Element) n).getAttribute("desc");
                    long ttl = getTimeLong(((Element) n).getAttribute("ttl"));
                    long maxMemorySize = getCapacityLong(((Element) n).getAttribute("max-memory-size"));
                    int maxSize = getInt(((Element) n).getAttribute("max-size"));

                    String algorithm = ((Element) n).getAttribute("algorithm");
                    if (algorithm == null || algorithm.trim().length() == 0) {
                        algorithm = CacheAlgorithm.LFU;
                    }
                    algorithm = algorithm.trim().toLowerCase();
                    if (!CacheAlgorithm.isSupportAlgorithm(algorithm)) {
                        throw new CacheException("Unknown cache algorithm:" + algorithm);
                    }
                    CacheConfig config = new CacheConfigImpl(id, desc, ttl, maxMemorySize, maxSize, algorithm);
                    cache.setCacheConfig(config);

                    cf.addCache(cache);
                }
            }

        } catch (SAXParseException e) {
            String msg = "Parsing error, line " + e.getLineNumber() + ", uri " + e.getSystemId() + "\n" + "   "
                    + e.getMessage();
            throw new CacheException(msg, e);
        } catch (SAXException | IOException | ParserConfigurationException e) {
            throw new CacheException(e.getMessage(), e);
        }
    }

    /**
     * string转为int
     *
     * @param value 待转换的string类型值
     * @return 转换结果
     */
    private static int getInt(String value) {
        if (value == null || value.trim().length() == 0) {
            return 0;
        }
        return Integer.parseInt(value);
    }

    /**
     * 时间转换，string转为long
     *
     * @param v 待转换的string类型值
     * @return 转换结果
     */
    private static long getTimeLong(String v) {
        if (v == null || v.trim().length() == 0) {
            return 0;
        }
        long r;
        String value = v.trim().toLowerCase();
        String lastSym = value.substring(value.length() - 1, value.length());
        if (lastSym.equalsIgnoreCase("s")) {
            r = Long.parseLong(value.substring(0, value.length() - 1)) * SECOND;
        } else if (lastSym.equalsIgnoreCase("m")) {
            r = Long.parseLong(value.substring(0, value.length() - 1)) * MINUTE;
        } else if (lastSym.equalsIgnoreCase("h")) {
            r = Long.parseLong(value.substring(0, value.length() - 1)) * HOUR;
        } else {
            r = Long.parseLong(value);
        }
        return r;
    }

    /**
     * 容量转换，string转为long
     *
     * @param v 待转换的string类型值
     * @return 转换结果
     */
    private static long getCapacityLong(String v) {
        if (v == null || v.trim().length() == 0) {
            return 0;
        }
        long r;
        String value = v.trim().toLowerCase();
        String lastSym = value.substring(value.length() - 1, value.length());
        if (lastSym.equalsIgnoreCase("k")) {
            r = Long.parseLong(value.substring(0, value.length() - 1)) * KB;
        } else if (lastSym.equalsIgnoreCase("m")) {
            r = Long.parseLong(value.substring(0, value.length() - 1)) * MB;
        } else {
            r = Long.parseLong(value);
        }
        return r;
    }

    /**
     * 私有构造函数Configurator
     */
    private Configurator() {

    }
}
