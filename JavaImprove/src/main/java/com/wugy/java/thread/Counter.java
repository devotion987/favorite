package com.wugy.java.thread;

import org.junit.Test;

/**
 * Java中volatile变量理解
 */
public class Counter {

    public int count = 0;
    public static int staticCount = 0;
    public volatile static int volatileCount = 0;

    public void inc() {
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        count++;
        staticCount++;
        volatileCount++;
    }

    @Test
    public void test() {
        for (int i = 0; i < 1000; i++) {
            new Thread(() -> inc()).start();
//            new Thread(new Runnable() {
//                public void run() {
//                    inc();
//                }
//            }).start();
        }
        System.out.println("运行结果：Counter.count = " + count);
        System.out.println("运行结果：Counter.staticCount = " + staticCount);
        System.out.println("运行结果：Counter.volatileCount = " + volatileCount);
    }
}