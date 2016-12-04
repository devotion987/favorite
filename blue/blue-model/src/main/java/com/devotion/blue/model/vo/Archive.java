package com.devotion.blue.model.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * 存档、归档。 和数据库无关的实体类。
 */
public class Archive {

    private String date; // 日期
    private long count; // 数量
    private List<Object> datas; // 数据列表

    public Archive() {
    }

    public Archive(String date, long count) {
        super();
        this.date = date;
        this.count = count;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public List<Object> getDatas() {
        return datas;
    }

    public void setDatas(List<Object> datas) {
        this.datas = datas;
    }

    public void addData(Object o) {
        if (datas == null) {
            datas = new ArrayList<>();
        }
        datas.add(o);
    }

}
