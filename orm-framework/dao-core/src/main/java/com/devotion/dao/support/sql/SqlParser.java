package com.devotion.dao.support.sql;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import org.springframework.beans.BeanUtils;

/**
 * SQL生成<br>
 */
public class SqlParser {

    /**
     * 表名
     */
    private String tableName;

    /**
     * id
     */
    private String id;

    /**
     * id名称
     */
    private String idName;

    /**
     * 是否是生成器对象
     */
    private boolean isGenerator = true;

    /**
     * 序列名称
     */
    private String seqName;

    /**
     * 列集合
     */
    private List<String> columnList = new ArrayList<>();

    /**
     * 列名集合
     */
    private List<String> columnNameList = new ArrayList<>();

    /**
     * 域列表
     */
    private List<String> fieldList = new ArrayList<>();

    /**
     * 插入
     */
    private String insert;

    /**
     * 更新
     */
    private String update;

    /**
     * 删除
     */
    private String delete;

    /**
     * 选择
     */
    private String select;

    /**
     * 动态更新
     */
    private String dynamicUpdate;

    /**
     * 全选
     */
    private String selectAll;

    /**
     * 分表路由
     */
    private String routeTable;

    /**
     * 是否实体表
     */
    private boolean isTableEntity = true;

    /**
     * 属性Map集合
     */
    private Map<String, Column> propertyMap = new HashMap<>();

    /**
     * 域Map集合
     */
    private Map<String, Column> fieldMap = new HashMap<>();

    /**
     * sql解析器
     *
     * @param clazz 解析类对象
     */
    public SqlParser(Class<?> clazz) {
        isTableEntity = setTable(clazz);
        if (isTableEntity) {
            /** 生成域列表 */
            setFieldList(clazz);
            /** 生成ID */
            setId(clazz);
            /** 生成列列表 */
            setColumnList(clazz);

            /** 生成插入语句 */
            setInsertSql();
            /** 生成更新语句 */
            setUpdateSql();
            /** 生成删除语句 */
            setDeleteSql();
            /** 生成选择语句 */
            setSelectSql();
            /** 生成全选语句 */
            setSelectAllSql();
        }
    }

    /**
     * 获取ID
     *
     * @return ID
     */
    public String getId() {
        return id;
    }

    /**
     * 获取“分表路由”关键字
     *
     * @return “分表路由”关键字
     */
    public String getRouteTable() {
        return routeTable;
    }

    /**
     * 获取“插入”关键字
     *
     * @return “插入”关键字
     */
    public String getInsert() {
        return insert;
    }

    /**
     * 获取“更新”关键字
     *
     * @return “更新”关键字
     */
    public String getUpdate() {
        return update;
    }

    /**
     * 获取“动态更新”关键字
     *
     * @param object Map对象
     * @return “动态更新”关键字
     */
    public String getDynamicUpdate(Map<String, ?> object) {
        setDynamicUpdateSql(object);
        return dynamicUpdate;
    }

    /**
     * 获取“删除”关键字
     *
     * @return “删除”关键字
     */
    public String getDelete() {
        return delete;
    }

    /**
     * 获取“选择”关键字
     *
     * @return “选择”关键字
     */
    public String getSelect() {
        return select;
    }

    /**
     * 获取“全选”关键字
     *
     * @return “全选”关键字
     */
    public String getSelectAll() {
        return selectAll;
    }

    /**
     * 判断是否是实体表
     *
     * @return 判断结果
     */
    public boolean isTableEntity() {
        return isTableEntity;
    }

    /**
     * 生成动态更新SQL语句
     *
     * @param object Map对象
     */
    private void setDynamicUpdateSql(Map<String, ?> object) {
        // StringBuffer sb = new StringBuffer("UPDATE ");
        StringBuilder sb = new StringBuilder("UPDATE ");

        sb.append(tableName).append(" SET ");
        int size = columnNameList.size();
        for (int i = 0; i < size; i++) {
            if (object.get(columnList.get(i)) == null || !propertyMap.get(columnNameList.get(i)).updatable()) {
                continue;
            }
            sb.append(columnNameList.get(i)).append(" = :").append(columnList.get(i));
            sb.append(", ");
        }
        sb.deleteCharAt(sb.length() - 2);
        sb.append(" WHERE ");
        sb.append(idName).append(" = :").append(id);
        dynamicUpdate = sb.toString();
    }

    /**
     * 生成“插入”SQL语句
     */
    private void setInsertSql() {

        // StringBuffer sb = new StringBuffer("INSERT INTO ");
        StringBuilder sb = new StringBuilder("INSERT INTO ");

        sb.append(tableName).append("(");

        if (!isGenerator) {
            if (idName != null && id != null) {
                sb.append(idName);
                sb.append(", ");
            }
        }
        int size = columnNameList.size();
        for (int i = 0; i < size; i++) {
            if (propertyMap.get(columnNameList.get(i)).insertable()) {
                sb.append(columnNameList.get(i));
                sb.append(", ");
            }
        }

        sb.deleteCharAt(sb.length() - 2);
        sb.append(") VALUES (");
        if (!isGenerator) {
            if (idName != null && id != null && seqName == null) {
                sb.append(":").append(id);
                sb.append(", ");
            } else {
                sb.append(seqName).append(".nextval, ");
            }
        }
        size = columnList.size();
        for (int i = 0; i < size; i++) {
            if (fieldMap.get(columnList.get(i)).insertable()) {
                sb.append(":").append(columnList.get(i));
                sb.append(", ");
            }
        }
        sb.deleteCharAt(sb.length() - 2);
        sb.append(")");
        insert = sb.toString();
    }

    /**
     * 生成“更新”SQL语句
     */
    private void setUpdateSql() {
        StringBuilder sb = new StringBuilder("UPDATE ");
        sb.append(tableName).append(" SET ");
        int size = columnNameList.size();
        for (int i = 0; i < size; i++) {
            if (propertyMap.get(columnNameList.get(i)).updatable()) {
                sb.append(columnNameList.get(i)).append(" = :").append(columnList.get(i));
                sb.append(", ");
            }
        }
        sb.deleteCharAt(sb.length() - 2);
        sb.append(" WHERE ");
        sb.append(idName).append(" = :").append(id);
        update = sb.toString();
    }

    /**
     * 生成“删除”SQL语句
     */
    private void setDeleteSql() {
        StringBuilder sb = new StringBuilder("DELETE FROM ");
        sb.append(tableName).append(" WHERE ");
        sb.append(idName).append(" = :").append(id);
        delete = sb.toString();
    }

    /**
     * 生成“选择”SQL语句
     */
    private void setSelectSql() {
        StringBuilder sb = new StringBuilder("SELECT ");
        List<String> tempList = new ArrayList<>(columnNameList);
        tempList.add(idName);
        int size = tempList.size();
        for (int i = 0; i < size; i++) {
            sb.append(tempList.get(i));
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append(" FROM ").append(tableName).append(" WHERE ");
        sb.append(idName).append(" = :").append(id);
        select = sb.toString();
    }

    /**
     * 生成“全选”SQL语句
     */
    private void setSelectAllSql() {
        StringBuilder sb = new StringBuilder("SELECT ");
        List<String> tempList = new ArrayList<>(columnNameList);
        tempList.add(idName);
        int size = tempList.size();
        for (int i = 0; i < size; i++) {
            sb.append(tempList.get(i));
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append(" FROM ").append(tableName);
        selectAll = sb.toString();
    }

    /**
     * 判断传入参数是否是实体表
     *
     * @param clazz 类对象
     * @return 判断结果
     */
    private boolean setTable(Class<?> clazz) {
        Entity entity = clazz.getAnnotation(Entity.class);
        if (entity == null) {
            return false;
        }
        tableName = entity.name().toUpperCase();
        return true;
    }

    /**
     * 通过反射获取field数组
     *
     * @param clazz 类对象
     */
    private void setFieldList(Class<?> clazz) {
        /** 获取类的所有声明变量 */
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            fieldList.add(field.getName());
        }
    }

    /**
     * 生成ID
     *
     * @param clazz 类对象
     */
    private void setId(Class<?> clazz) {
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(Id.class)) {
                Column column = method.getAnnotation(Column.class);
                idName = column.name();
                routeTable = column.table();
                id = BeanUtils.findPropertyForMethod(method).getName();
                GeneratedValue generatedValue = method.getAnnotation(GeneratedValue.class);
                SequenceGenerator seqGen = method.getAnnotation(SequenceGenerator.class);

                if (generatedValue != null && generatedValue.strategy() != null
                        && generatedValue.strategy().compareTo(GenerationType.AUTO) != 0) {
                    isGenerator = false;
                    if (seqGen != null) {
                        seqName = seqGen.sequenceName();
                    }
                } else {
                    isGenerator = true;
                }
            }
        }
    }

    /**
     * 生成列字段的列表
     *
     * @param clazz 类对象
     */
    private void setColumnList(Class<?> clazz) {
        Method[] methods = clazz.getMethods();
        Field[] fields = clazz.getDeclaredFields();
        Field[] superFields = clazz.getSuperclass().getDeclaredFields();
        for (Method method : methods) {
            if (method.isAnnotationPresent(Column.class) && !method.isAnnotationPresent(Id.class)) {
                PropertyDescriptor descriptor = BeanUtils.findPropertyForMethod(method);
                if (isTransient(fields, descriptor.getName()) || isTransient(superFields, descriptor.getName())) {
                    continue;
                }
                Column columnAnnotaion = method.getAnnotation(Column.class);
                columnNameList.add(columnAnnotaion.name());
                columnList.add(BeanUtils.findPropertyForMethod(method).getName());
                propertyMap.put(columnAnnotaion.name(), columnAnnotaion);
                fieldMap.put(BeanUtils.findPropertyForMethod(method).getName(), columnAnnotaion);
            }
        }
    }

    /**
     * 判断field是否是临时性的
     *
     * @param fields   域
     * @param fileName 域名
     * @return 判断结果
     */
    private boolean isTransient(Field[] fields, String fileName) {
        for (Field field : fields) {
            if (field.getName().equals(fileName) && Modifier.isTransient(field.getModifiers())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取ID名称
     *
     * @return ID名称
     */
    public String getIdName() {
        return idName;
    }

    /**
     * 设置ID名称
     *
     * @param idName ID名称
     */
    public void setIdName(String idName) {
        this.idName = idName;
    }

    /**
     * 获取序列名称
     *
     * @return 序列名称
     */
    public String getSeqName() {
        return seqName;
    }

    /**
     * 设置序列名称
     *
     * @param seqName 序列名称
     */
    public void setSeqName(String seqName) {
        this.seqName = seqName;
    }

    /**
     * 设置ID
     *
     * @param id ID
     */
    public void setId(String id) {
        this.id = id;
    }
}
