package com.wugy.javaPattern.observer;

/**
 * Created by Administrator on 2016/3/23.
 */
public interface Subject {

    void registerObserver(Observer o);

    void removeObserver(Observer o);

    void notifyObservers();
}
