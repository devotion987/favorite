package com.wugy.javaPattern.strategy;

/**
 * Created by Administrator on 2016/3/21.
 */
abstract public  class Duck {

    FlyBehavier flyBehavier;
    QuackBehavier quackBehavier;

    public void performFly() {
        flyBehavier.fly();
    }

    public void performQuack() {
        quackBehavier.quack();
    }

    public void swim() {
        System.out.println("All ducks float, even decoys!");
    }

    public void setFlyBehavier(FlyBehavier fb) {
        flyBehavier = fb;
    }

    public void setQuackBehavier(QuackBehavier qb) {
        quackBehavier = qb;
    }

    abstract public void display();
}
