package com.devotion.blue.search;

import com.jfinal.plugin.activerecord.Page;

public class SearcherKit {

	private static ISearcher mSearcher;

	static void init(ISearcher mSearcher) {
		SearcherKit.mSearcher = mSearcher;
	}

	public static void add(SearcherBean bean) {
		checkSearcher();
		mSearcher.addBean(bean);
	}

	public static void delete(String beanId) {
		checkSearcher();
		mSearcher.deleteBean(beanId);
	}

	public static void update(SearcherBean bean) {
		checkSearcher();
		mSearcher.updateBean(bean);
	}

	/**
	 * 执行搜索
	 * 
	 * @param keyword
	 * @return List<SearcherBean>
	 */
	public static Page<SearcherBean> search(String keyword) {
		checkSearcher();
		return mSearcher.search(keyword, null);
	}

	public static Page<SearcherBean> search(String keyword, String module) {
		checkSearcher();
		return mSearcher.search(keyword, module);
	}

	public static Page<SearcherBean> search(String queryString, int pageNum, int pageSize) {
		checkSearcher();
		return mSearcher.search(queryString, null, pageNum, pageSize);
	}

	public static Page<SearcherBean> search(String queryString, String module, int pageNum, int pageSize) {
		checkSearcher();
		return mSearcher.search(queryString, module, pageNum, pageSize);
	}

	public static void checkSearcher() {
		if (mSearcher == null) {
			throw new RuntimeException("must init searcher before,please invoke SearchFactory.use() to init.");
		}
	}

}
