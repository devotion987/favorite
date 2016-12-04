package com.devotion.blue.search;

import java.util.ArrayList;
import java.util.List;

import com.devotion.blue.model.Content;
import com.devotion.blue.model.query.ContentQuery;
import com.devotion.blue.model.template.TemplateManager;
import com.devotion.blue.model.template.TplModule;
import com.devotion.blue.utils.StringUtils;
import com.jfinal.plugin.activerecord.Page;

import javax.swing.text.AbstractDocument;

public class DbSearcher implements ISearcher {

    @Override
    public void init() {

    }

    @Override
    public void addBean(SearcherBean bean) {

    }

    @Override
    public void deleteBean(String beanId) {

    }

    @Override
    public void updateBean(SearcherBean bean) {

    }

    @Override
    public Page<SearcherBean> search(String keyword, String module) {
        return search(keyword, module, 1, 10);
    }

    @Override
    public Page<SearcherBean> search(String keyword, String module, int pageNum, int pageSize) {

        String[] moduleStrings;
        if (StringUtils.isNotBlank(module)) {
            moduleStrings = new String[]{module};
        } else {
            List<TplModule> modules = TemplateManager.me().currentTemplateModules();
            if (modules == null || modules.size() == 0) {
                return null;
            }

            moduleStrings = new String[modules.size()];
            for (int i = 0; i < moduleStrings.length; i++) {
                moduleStrings[i] = modules.get(i).getName();
            }
        }

        Page<Content> cpage = ContentQuery.me().paginate(pageNum, pageSize, moduleStrings, keyword,
                Content.STATUS_NORMAL, null, null, null, null);

        if (cpage != null) {
            List<SearcherBean> datas = new ArrayList<>();
            for (Content c : cpage.getList()) {
                datas.add(new SearcherBean(c.getId().toString(), c.getTitle(), c.getSummary(), c.getText(), c.getUrl(), c.getCreated(), c));
            }

            return new Page<>(datas, cpage.getPageNumber(), cpage.getPageSize(), cpage.getTotalPage(), cpage.getTotalRow());
        }

        return null;

    }

}
