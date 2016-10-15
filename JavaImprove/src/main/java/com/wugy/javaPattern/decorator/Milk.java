package com.wugy.javaPattern.decorator;

/**
 * Created by Administrator on 2016/3/27 0027.
 */
public class Milk extends CondimentDecorator {

    Beverage beverage;

    public Milk(Beverage beverage) {
        this.beverage = beverage;
    }

    @Override
    public String getDescription() {
        return beverage.getDescription() + ", Milk";
    }

    @Override
    public double cost() {
        return .20 + beverage.cost();
    }
}
