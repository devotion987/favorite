package com.wugy.javaPattern.observer;

/**
 * Created by Administrator on 2016/3/23.
 */
public interface Observer {

    /**
     * 当气象观测值改变时，这些状态值当作方法的参数，传递给观察者
     *
     * @param temp
     * @param humidity
     * @param pressure
     */
    void update(float temp, float humidity, float pressure);
}
