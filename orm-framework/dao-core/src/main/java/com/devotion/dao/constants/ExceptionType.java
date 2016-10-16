package com.devotion.dao.constants;

/**
 * 异常类型枚举
 */
public enum ExceptionType {

    /**
     * web layer exception
     */
    EXCEPTION_WEB(0, "error.web.000", "web layer exception"),

    /**
     * service layer exception
     */
    EXCEPTION_SVS(1, "error.svs.001", "service layer exception"),

    /**
     * dal layer exception
     */
    EXCEPTION_DAO(2, "error.dao.002", "dao layer exception"),

    /**
     * integration layer exception
     */
    EXCEPTION_INT(3, "error.int.003", "integration layer exception"),

    /**
     * user define exception
     */
    EXCEPTION_BIZ(4, "error.biz.004", "user define exception"),

    /**
     * default exception
     */
    EXCEPTION_DEF(5, "error.def.005", "default exception"),

    DB_CONN_ERROR(6, "error.db.006", "connect database fail"),
    DB_ACCESS_ERROR(7, "error.db.007", "connect database fail");

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * 索引
     */
    private int index;
    /**
     * 错误代码
     */
    private String code;
    /**
     * 描述
     */
    private String desc;

    /**
     * 获取异常描述信息
     *
     * @return 异常描述信息
     */
    public String getDesc() {
        return desc;
    }

    /**
     * 构造方法
     *
     * @param index 索引
     * @param code  编码
     * @param desc  描述
     */
    ExceptionType(int index, String code, String desc) {
        this.index = index;
        this.code = code;
        this.desc = desc;
    }

    /**
     * 返回索引
     *
     * @return the index
     */
    public int getIndex() {
        return index;
    }

}
