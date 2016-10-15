package com.wugy.javaPattern.decorator;

/**
 * 浓咖啡
 * <p>
 * Created by Administrator on 2016/3/27 0027.
 */
public class Espresso extends Beverage {

    public Espresso() {
        description = "Espresso";
    }

    @Override
    public double cost() {
        return 1.99;
    }
}
