package com.devotion.dao.resource.parse;

/**
 * SQL映射
 */
public class SqlBean {

    /**
     * id
     */
    private String id;

    /**
     * 是否读取
     */
    private boolean isRead;

    /**
     * 具体内容
     */
    private String content;

    /**
     * 数据源名称
     */
    private String dsName;

    /**
     * 数据库类型
     */
    private String dbType;

    /**
     * 构造方法重载
     *
     * @param id      id
     * @param isRead  是否可读
     * @param content BEAN内容
     * @param dsName  数据源名称
     */
    public SqlBean(String id, String isRead, String content, String dsName, String dbType) {
        super();
        this.id = id;
        if (isRead != null) {
            try {
                this.isRead = Boolean.valueOf(isRead);
            } catch (NumberFormatException e) {
                this.isRead = Boolean.FALSE;
            }
        }
        this.content = content;
        this.dsName = dsName;

        this.dbType = dbType;
    }

    /**
     * 构造方法重载
     *
     * @param id      SQLID
     * @param isRead  是否可读
     * @param content BEAN内容
     * @param dsName  数据源名称
     */
    public SqlBean(String id, boolean isRead, String content, String dsName, String dbType) {
        super();
        this.id = id;
        this.isRead = isRead;
        this.content = content;
        this.dsName = dsName;

        this.dbType = dbType;
    }

    /**
     * 构造方法
     */
    public SqlBean() {
        super();
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
     * 设置ID
     *
     * @param id ID
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 是否可读
     *
     * @return 布尔值
     */
    public boolean isRead() {
        return isRead;
    }

    /**
     * 设置是否可读
     *
     * @param isRead 是否可读
     */
    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }

    /**
     * 获取BEAN内容
     *
     * @return BEAN内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置BEAN内容
     *
     * @param content BEAN内容
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 获取数据源名称
     *
     * @return 数据源名称
     */
    public String getDsName() {
        return dsName;
    }

    /**
     * 设置数据源名称
     *
     * @param dsName 数据源名称
     */
    public void setDsName(String dsName) {
        this.dsName = dsName;
    }

    /**
     * 获取数据源类型
     *
     * @return 数据源类型
     */
    public String getDbType() {
        return dbType;
    }

    /**
     * 设置数据源类型
     *
     * @param dbType 数据源类型
     */
    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

}
