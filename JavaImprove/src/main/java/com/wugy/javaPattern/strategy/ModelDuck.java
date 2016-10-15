package com.wugy.javaPattern.strategy;

/**
 * Created by Administrator on 2016/3/21.
 */
public class ModelDuck extends Duck {

    public ModelDuck() {
        flyBehavier = new FlyNoWay();
        quackBehavier = new Quack();
    }

    @Override
    public void display() {
        System.out.println("I am a model duck!");
    }
}
