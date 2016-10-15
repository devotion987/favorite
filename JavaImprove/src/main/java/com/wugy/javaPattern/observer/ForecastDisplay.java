package com.wugy.javaPattern.observer;

/**
 * Created by Administrator on 2016/3/23.
 */
public class ForecastDisplay implements Observer, DisplayElement {

	private float currentPressure = 29.92f;
	private float lastPressure;
	private WeatherData weatherData;

	public ForecastDisplay(WeatherData weatherData) {
		this.weatherData = weatherData;
		weatherData.registerObserver(this);
	}

	public void update(float temp, float humidity, float pressure) {
		lastPressure = currentPressure;
		currentPressure = pressure;
		display();
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

	public float getCurrentPressure() {
		return currentPressure;
	}

	public float getLastPressure() {
		return lastPressure;
	}

	public WeatherData getWeatherData() {
		return weatherData;
	}

}
