package com.devotion.dao.pagination;

import java.io.Serializable;

/**
 * 功能描述： 分页bean
 */
public class Pagination implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 7554108855455655618L;

    /**
     * 默认每页显示条数
     */
    public static final int PAGESIZE = 20;

    /**
     * 每页显示条数
     */
    private int pageSize;

    /**
     * 总行数
     */
    private int totalRows = -1;

    /**
     * 当前页
     */
    private int currentPage = 1;

    public Pagination() {
        this(PAGESIZE, 1);
    }

    /**
     * 构造方法
     *
     * @param pageSize    每页显示条数
     * @param currentPage 当前页
     */
    public Pagination(int pageSize, int currentPage) {
        this.pageSize = pageSize;
        this.currentPage = currentPage;
    }

    /**
     * 获取当前页第一行的索引
     *
     * @return 当前页第一行的索引
     */
    public int getFirstRowIndex() {
        return (getCurrentPage() - 1) * getPageSize();
    }

    /**
     * 获取每页显示条数
     *
     * @return 每页显示条数
     */
    public int getPageSize() {
        return this.pageSize;
    }

    /**
     * 获取总条数
     *
     * @return 总条数
     */
    public int getTotalRows() {
        return this.totalRows;
    }

    /**
     * 获取当前页
     *
     * @return 当前页
     */
    public int getCurrentPage() {
        return currentPage;
    }

    /**
     * 设置总行数
     *
     * @param totalRows 总行数
     */
    public void setTotalRows(int totalRows) {
        this.totalRows = totalRows;
        if (this.totalRows != -1 && getCurrentPage() > getPageCount()) {
            setCurrentPage(1);
        }
    }

    /**
     * 设置每页显示条数
     *
     * @param pageSize 每页显示条数
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * 设置当前页
     *
     * @param currentPage 当前页
     */
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    /**
     * 获取总页数
     *
     * @return 总页数
     */
    public int getPageCount() {
        if (pageSize == 0) {
            return 0;
        }
        if (getTotalRows() > 0) {
            return (totalRows / pageSize) + (totalRows % pageSize == 0 ? 0 : 1);
        }
        return 0;
    }

}
