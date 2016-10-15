package com.wugy.javaPattern.strategy;


import org.junit.Test;

/**
 * Created by Administrator on 2016/3/21.
 */
public class DuckTest {

    @Test
    public void test1() {
        Duck mallardDuck = new MallardDuck();
        mallardDuck.performFly();
        mallardDuck.performQuack();
    }

    @Test
    public void test2() {
        Duck modelDuck = new ModelDuck();
        modelDuck.performFly();
        modelDuck.setFlyBehavier(new FlyRocketPowered());
        modelDuck.performFly();
    }
}
