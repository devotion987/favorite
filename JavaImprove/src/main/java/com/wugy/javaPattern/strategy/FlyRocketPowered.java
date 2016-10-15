package com.wugy.javaPattern.strategy;

/**
 * Created by Administrator on 2016/3/21.
 */
public class FlyRocketPowered implements FlyBehavier {

    @Override
    public void fly() {
        System.out.println("I am flying with a rocket!");
    }
}
