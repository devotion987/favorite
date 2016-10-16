package com.devotion.dao.support.sql;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import com.devotion.dao.constants.ExceptionType;
import com.devotion.dao.exception.BaseException;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * FreeMaker解析，实现SQL渲染<br>
 */
public class FreeMakerParser {

    // Utility classes, which are a collection of static members, are not meant
    // to be instantiated.
    // Even abstract utility classes, which can be extended, should not have
    // public constructors.
    private FreeMakerParser() {
    }

    /**
     * 默认模板KEY
     */
    private static final String DEFAULT_TEMPLATE_KEY = "default_template_key";

    /**
     * 默认模板表达式
     */
    private static final String DEFAULT_TEMPLATE_EXPRESSION = "default_template_expression";

    /**
     * 配置器
     */
    @SuppressWarnings("deprecation")
    private static final Configuration CONFIGURER = new Configuration();

    static {
        CONFIGURER.setClassicCompatible(true);
    }

    /**
     * 配置SQL表达式缓存
     */
    private static Map<String, Template> templateCache = new HashMap<>();
    /**
     * 分库表达式缓存
     */
    private static Map<String, Template> expressionCache = new HashMap<>();

    /**
     * 解析处理方法
     *
     * @param expression 表达式
     * @param root       根结点
     * @return 处理结果字符串
     */
    public static String process(String expression, Map<String, Object> root) {
        StringWriter out = null;
        Template template = null;
        /** 表达式注入模板 */
        try {
            if (expressionCache.get(expression) != null) {
                template = expressionCache.get(expression);
            }
            if (template == null) {
                template = createTemplate(DEFAULT_TEMPLATE_EXPRESSION, new StringReader(expression));
                expressionCache.put(expression, template);
            }
            out = new StringWriter();
            template.process(root, out);
            return out.toString();
        } catch (Exception e) {
            throw new BaseException(ExceptionType.EXCEPTION_DEF.getCode(), e, null, ExceptionType.EXCEPTION_DAO);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                // return null;
            }
        }
    }

    /**
     * 创建模板
     *
     * @param templateKey 模板键值
     * @param reader      待处理字符流
     * @return 模板
     * @throws IOException IO异常
     */
    private static Template createTemplate(String templateKey, StringReader reader) throws IOException {
        // Template template = new Template(DEFAULT_TEMPLATE_KEY, reader,
        // CONFIGURER);
        // update by guohauc@cn.ibm.com 20151124
        Template template = new Template(templateKey, reader, CONFIGURER);

        template.setNumberFormat("#");
        return template;
    }

    /**
     * 解析处理重载方法
     *
     * @param root  root对象
     * @param sql   SQL串
     * @param sqlId SQLID
     * @return StringWriter字符串
     */
    public static String process(Map<String, Object> root, String sql, String sqlId) {
        StringReader reader = null;
        StringWriter out = null;
        Template template = null;
        /** 表达式注入模板 */
        try {
            if (templateCache.get(sqlId) != null) {
                template = templateCache.get(sqlId);
            }
            if (template == null) {
                reader = new StringReader(sql);
                template = createTemplate(DEFAULT_TEMPLATE_KEY, reader);
                templateCache.put(sqlId, template);
            }
            out = new StringWriter();
            template.process(root, out);
            return out.toString();
        } catch (Exception e) {
            throw new BaseException("error.dal.005", e, null, ExceptionType.EXCEPTION_DAO);
        } finally {
            if (reader != null) {
                reader.close();
            }
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                // return null;
            }
        }
    }
}
