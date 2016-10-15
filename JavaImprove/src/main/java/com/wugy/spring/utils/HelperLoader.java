package com.wugy.spring.utils;

import com.wugy.java.utils.ClassUtil;
import com.wugy.spring.annotation.aop.AopHelper;
import com.wugy.spring.annotation.mvc.ActionHelper;
import com.wugy.spring.jdbc.DatabaseHelper;
import com.wugy.spring.jdbc.orm.EntityHelper;
import com.wugy.spring.plugin.PluginHelper;

/**
 * 加载相应的 Helper 类
 *
 * @author devotion
 */
public final class HelperLoader {

	public static void init() {
		// 定义需要加载的 Helper 类
		Class<?>[] classList = { DatabaseHelper.class, EntityHelper.class, ActionHelper.class, BeanHelper.class,
				AopHelper.class, IocHelper.class, PluginHelper.class, };
		// 按照顺序加载类
		for (Class<?> cls : classList) {
			ClassUtil.loadClass(cls.getName());
		}
	}
}
