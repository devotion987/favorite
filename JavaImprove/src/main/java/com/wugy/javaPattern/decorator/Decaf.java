package com.wugy.javaPattern.decorator;

/**
 * 脱咖啡因咖啡
 * <p>
 * Created by Administrator on 2016/3/27 0027.
 */
public class Decaf extends Beverage {

    public Decaf() {
        description = "Decaf Coffee";
    }

    @Override
    public double cost() {
        return 1.05;
    }
}
