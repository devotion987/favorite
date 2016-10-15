package com.wugy.javaPattern.decorator;

/**
 * 饮料
 * <p>
 * Created by Administrator on 2016/3/27 0027.
 */
abstract public class Beverage {

    String description = "Unknown Beverage";

    public String getDescription() {
        return description;
    }

    abstract public double cost();
}
