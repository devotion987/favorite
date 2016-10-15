package com.wugy.javaPattern.observer;

import java.util.*;

/**
 * Created by Administrator on 2016/3/24.
 */
public class ForecastDisplay2 implements java.util.Observer, DisplayElement {

    private float currentPressure = 29.92f;
    private float lastPressure;

    public ForecastDisplay2(Observable observable) {
        observable.addObserver(this);
    }

    public void update(Observable observable, Object arg) {
        if (observable instanceof WeatherData2) {
            WeatherData2 weatherData2 = (WeatherData2) observable;
            lastPressure = currentPressure;
            currentPressure = weatherData2.getPressure();
            display();
        }
    }

    public void display() {
        System.out.print("Forecast: ");
        if (currentPressure > lastPressure) {
            System.out.println("Improving weather on the way! \n");
        } else if (currentPressure == lastPressure) {
            System.out.println("More of the same \n");
        } else if (currentPressure < lastPressure) {
            System.out.println("Watch out for cooler, rainy weather \n");
        }
    }
}
