package com.wugy.javaPattern.decorator;

/**
 * Created by Administrator on 2016/3/27 0027.
 */
public class DarkRoast extends Beverage {

    public DarkRoast() {
        description = "Dark Roast Coffee";
    }

    @Override
    public double cost() {
        return .99;
    }
}
