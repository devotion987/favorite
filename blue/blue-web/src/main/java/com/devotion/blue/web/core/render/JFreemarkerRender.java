package com.devotion.blue.web.core.render;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.devotion.blue.model.query.OptionQuery;
import com.devotion.blue.utils.Consts;
import com.devotion.blue.utils.StringUtils;
import com.devotion.blue.web.core.cache.ActionCacheManager;
import com.devotion.blue.web.core.JPress;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.jfinal.render.FreeMarkerRender;
import com.jfinal.render.RenderException;

import freemarker.template.Template;

public class JFreemarkerRender extends FreeMarkerRender {

	private boolean enableCdnProcess;

	public JFreemarkerRender(String view, boolean enableCdnProcess) {
		super(view);
		this.enableCdnProcess = enableCdnProcess;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void render() {

		Map<String, Object> jpTags = new HashMap<>();
		jpTags.putAll(JPress.jPressTags);

		Map data = new HashMap();
		for (Enumeration<String> attrs = request.getAttributeNames(); attrs.hasMoreElements();) {
			String attrName = attrs.nextElement();
			if (attrName.startsWith("jp.")) {
				jpTags.put(attrName.substring(3), request.getAttribute(attrName));
			} else {
				data.put(attrName, request.getAttribute(attrName));
			}
		}
		data.put("jp", jpTags);

		String htmlContent = getHtmlContent(data);

		// 排除 后台的CDN 处理，防止外一CDN出问题导致后台无法登录
		if (enableCdnProcess) {
			htmlContent = processCDN(htmlContent); // CDN处理
		}

		if (!ActionCacheManager.isCloseActionCache() && ActionCacheManager.isEnableCache(request)) {
			WriterHtml(htmlContent, ActionCacheManager.getCacheContentType(request), true);
		}

		WriterHtml(htmlContent, getContentType(), false);

	}

	private void WriterHtml(String htmlContent, String contentType, boolean isPutToCache) {
		response.setContentType(contentType);
		PrintWriter responseWriter = null;
		try {
			responseWriter = response.getWriter();
			responseWriter.write(htmlContent);
			if (isPutToCache) {
				ActionCacheManager.putCache(request, htmlContent);
			}
		} catch (Exception e) {
			if (JPress.isDevMode()) {
				e.printStackTrace();
			}
			throw new RenderException(e);
		} finally {
			close(responseWriter);
		}
	}

	@SuppressWarnings("rawtypes")
	private String getHtmlContent(Map data) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		OutputStreamWriter osWriter = null;
		try {
			osWriter = new OutputStreamWriter(baos, Consts.CHARTSET_UTF8);
			Template template = getConfiguration().getTemplate(view);
			template.process(data, osWriter);
			osWriter.flush();
			return baos.toString(Consts.CHARTSET_UTF8);
		} catch (Exception e) {
			if (JPress.isDevMode()) {
				e.printStackTrace();
			}
			throw new RenderException(e);
		} finally {
			close(baos);
			close(osWriter);
		}
	}

	private void close(Writer writer) {
		if (writer != null) {
			try {
				writer.close();
			} catch (IOException e) {
			}
		}
	}

	private void close(OutputStream stream) {
		if (stream != null) {
			try {
				stream.close();
			} catch (IOException e) {
			}
		}
	}

	private String processCDN(String content) {
		Boolean cdnEnable = OptionQuery.me().findValueAsBool("cdn_enable");
		if (cdnEnable == null || !cdnEnable) {
			return content;
		}

		String cdnDomain = OptionQuery.me().findValue("cdn_domain");
		if (StringUtils.isBlank(cdnDomain)) {
			return content;
		}

		Document doc = Jsoup.parse(content);

		Elements jsElements = doc.select("script[src]");
		replace(jsElements, "src", cdnDomain);

		Elements imgElements = doc.select("img[src]");
		replace(imgElements, "src", cdnDomain);

		Elements linkElements = doc.select("link[href]");
		replace(linkElements, "href", cdnDomain);

		return doc.toString();

	}

	public static void replace(Elements elements, String attrName, String cdnDomain) {
		Iterator<Element> iterator = elements.iterator();
		while (iterator.hasNext()) {
			Element element = iterator.next();
			if (element.hasAttr("cdn-exclude")) {
				continue;
			}
			String url = element.attr(attrName);
			if (isExcludeUrl(url))
				continue;
			if (url != null && url.startsWith("/")) {
				url = cdnDomain + url;
			}
			element.attr(attrName, url);
		}
	}

	private static boolean isExcludeUrl(String url) {
		if (StringUtils.isBlank(url))
			return false;

		String cdn_exclude_files = OptionQuery.me().findValue("cdn_exclude_files");
		if (StringUtils.isNotBlank(cdn_exclude_files)) {
			if (cdn_exclude_files.contains(url) || url.contains("/counter")) {
				return true;
			}

			String[] lines = cdn_exclude_files.split("\\n");
			for (String regex : lines) {
				if (StringUtils.match(url, regex)) {
					return true;
				}
			}
		}

		return false;
	}

}
