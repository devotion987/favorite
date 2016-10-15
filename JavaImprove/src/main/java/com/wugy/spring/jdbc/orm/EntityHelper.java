package com.wugy.spring.jdbc.orm;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.wugy.java.utils.StringUtil;
import com.wugy.spring.utils.ClassHelper;
import com.wugy.spring.utils.ObjectUtil;

/**
 * 初始化 Entity 结构
 *
 * @author devotion
 */
public class EntityHelper {

	/**
	 * 实体类 => 表名
	 */
	private static final Map<Class<?>, String> entityClassTableNameMap = new HashMap<Class<?>, String>();

	/**
	 * 实体类 => (字段名 => 列名)
	 */
	private static final Map<Class<?>, Map<String, String>> entityClassFieldMap = new HashMap<Class<?>, Map<String, String>>();

	static {
		// 获取并遍历所有实体类
		List<Class<?>> entityClassList = ClassHelper.getClassListByAnnotation(Entity.class);
		for (Class<?> entityClass : entityClassList) {
			initEntityNameMap(entityClass);
			initEntityFieldMapMap(entityClass);
		}
	}

	private static void initEntityNameMap(Class<?> entityClass) {
		// 判断该实体类上是否存在 Table 注解
		String tableName;
		if (entityClass.isAnnotationPresent(Table.class)) {
			// 若已存在，则使用该注解中定义的表名
			tableName = entityClass.getAnnotation(Table.class).value();
		} else {
			// 若不存在，则将实体类名转换为下划线风格的表名
			tableName = StringUtil.camelhumpToUnderline(entityClass.getSimpleName());
		}
		entityClassTableNameMap.put(entityClass, tableName);
	}

	private static void initEntityFieldMapMap(Class<?> entityClass) {
		// 获取并遍历该实体类中所有的字段（不包括父类中的方法）
		Field[] fields = entityClass.getDeclaredFields();
		if (ArrayUtils.isNotEmpty(fields)) {
			// 创建一个 fieldMap（用于存放列名与字段名的映射关系）
			Map<String, String> fieldMap = new HashMap<String, String>();
			for (Field field : fields) {
				String fieldName = field.getName();
				String columnName;
				// 判断该字段上是否存在 Column 注解
				if (field.isAnnotationPresent(Column.class)) {
					// 若已存在，则使用该注解中定义的列名
					columnName = field.getAnnotation(Column.class).value();
				} else {
					// 若不存在，则将字段名转换为下划线风格的列名
					columnName = StringUtil.camelhumpToUnderline(fieldName);
				}
				fieldMap.put(fieldName, columnName);
			}
			entityClassFieldMap.put(entityClass, fieldMap);
		}
	}

	public static String getTableName(Class<?> entityClass) {
		return entityClassTableNameMap.get(entityClass);
	}

	public static Map<String, String> getFieldMap(Class<?> entityClass) {
		return entityClassFieldMap.get(entityClass);
	}

	public static Map<String, String> getColumnMap(Class<?> entityClass) {
		return ObjectUtil.invert(getFieldMap(entityClass));
	}

	public static String getColumnName(Class<?> entityClass, String fieldName) {
		String columnName = getFieldMap(entityClass).get(fieldName);
		return StringUtils.isNotEmpty(columnName) ? columnName : fieldName;
	}
}
