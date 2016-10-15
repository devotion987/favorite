package com.wugy.javaPattern.strategy;

/**
 * Created by Administrator on 2016/3/21.
 */
public class MallardDuck extends Duck {

    public MallardDuck() {
        quackBehavier = new Quack();
        flyBehavier = new FlyWithWings();
    }

    @Override
    public void display() {
        System.out.println("I am a real Mallard duck!");
    }
}
