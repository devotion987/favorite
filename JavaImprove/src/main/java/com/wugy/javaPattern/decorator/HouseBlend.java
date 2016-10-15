package com.wugy.javaPattern.decorator;

/**
 * 混合
 * <p>
 * Created by Administrator on 2016/3/27 0027.
 */
public class HouseBlend extends Beverage {

    public HouseBlend() {
        description = "House Blend Coffee";
    }

    @Override
    public double cost() {
        return .89;
    }
}
