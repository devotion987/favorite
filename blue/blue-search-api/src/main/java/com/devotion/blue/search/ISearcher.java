package com.devotion.blue.search;

import com.jfinal.plugin.activerecord.Page;

public interface ISearcher {

    void init();

    void addBean(SearcherBean bean);

    void deleteBean(String beanId);

    void updateBean(SearcherBean bean);

    Page<SearcherBean> search(String keyword, String module);

    public Page<SearcherBean> search(String queryString, String module, int pageNum, int pageSize);
}
