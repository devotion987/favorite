package com.wugy.javaPattern.strategy;

/**
 * Created by Administrator on 2016/3/21.
 */
public class Quack implements QuackBehavier{

    @Override
    public void quack() {
        System.out.println("Quack");
    }
}
