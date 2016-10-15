package com.wugy.javaPattern.observer;

import org.junit.Test;

/**
 * Created by Administrator on 2016/3/23.
 */
public class WeatherStation {

	@SuppressWarnings("unused")
	@Test
	public void testObserver() {
		WeatherData weatherData = new WeatherData();
		CurrentConditionsDisplay currentDisplay = new CurrentConditionsDisplay(weatherData);
		StatisticsDisplay statisticsDisplay = new StatisticsDisplay(weatherData);
		ForecastDisplay forecastDisplay = new ForecastDisplay(weatherData);

		weatherData.setMeasurements(80, 65, 30.4f);
		weatherData.setMeasurements(82, 75, 40.4f);
		weatherData.setMeasurements(70, 95, 60.4f);

		System.out.println("===========================");

		WeatherData2 weatherData2 = new WeatherData2();
		CurrentConditionsDisplay2 currentDisplay2 = new CurrentConditionsDisplay2(weatherData2);
		StatisticsDisplay2 statisticsDisplay2 = new StatisticsDisplay2(weatherData2);
		ForecastDisplay2 forecastDisplay2 = new ForecastDisplay2(weatherData2);
		weatherData2.setMeasurements(80, 65, 30.4f);
		weatherData2.setMeasurements(82, 75, 40.4f);
		weatherData2.setMeasurements(70, 95, 60.4f);
	}
}
