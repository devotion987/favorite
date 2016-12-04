package com.devotion.blue.model.route;

import com.devotion.blue.model.Content;
import com.devotion.blue.model.query.ContentQuery;
import com.devotion.blue.utils.Consts;
import com.devotion.blue.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PageRouter extends RouterConverter {

    public static String getRouter(Content content) {
        String url = SLASH + content.getSlug();

        if (enalbleFakeStatic()) {
            url += getFakeStaticSuffix();
        }
        return url;
    }

    @Override
    public String converter(String target, HttpServletRequest request, HttpServletResponse response) {

        String[] targetDirs = parseTarget(target);
        if (targetDirs == null || targetDirs.length != 1) {
            return null;
        }

        String slug = targetDirs[0];
        Content content = ContentQuery.me().findBySlug(StringUtils.urlDecode(slug));
        if (null != content && Consts.MODULE_PAGE.equals(content.getModule())) {
            return Consts.ROUTER_CONTENT + SLASH + slug;
        }

        return null;
    }

}
