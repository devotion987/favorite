package com.wugy.javaPattern.strategy;

/**
 * Created by wugy on 2016/3/21.
 */
public class FlyWithWings implements FlyBehavier {

    @Override
    public void fly() {
        System.out.println("I am flying!");
    }
}
