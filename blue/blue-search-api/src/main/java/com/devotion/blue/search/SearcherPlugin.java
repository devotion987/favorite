package com.devotion.blue.search;

import java.util.List;

import com.devotion.blue.utils.ClassUtils;
import com.jfinal.log.Log;
import com.jfinal.plugin.IPlugin;


public class SearcherPlugin implements IPlugin {

	static final Log log = Log.getLog(SearcherPlugin.class);

	private static ISearcher mSearcher;

	public static void initSearcher(Class<? extends ISearcher> clazz) {
		try {
			mSearcher = (ISearcher) clazz.newInstance();
			mSearcher.init();
			SearcherKit.init(mSearcher);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean start() {
		List<Class<ISearcher>> list = ClassUtils.scanSubClass(ISearcher.class, true);

		if (list == null || list.isEmpty()) {
			log.error("cant scan ISearcher implement class in class path.");
			return true;
		}

		if (list.size() > 1) {
			log.warn("there are too many searcher");
		}

		initSearcher(list.get(0));

		return true;
	}

	@Override
	public boolean stop() {
		return true;
	}

}
