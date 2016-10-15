package com.wugy.javaPattern.strategy;

/**
 * Created by Administrator on 2016/3/21.
 */
public class FlyNoWay implements FlyBehavier{


    @Override
    public void fly() {
        System.out.println("I can not fly!");
    }
}
