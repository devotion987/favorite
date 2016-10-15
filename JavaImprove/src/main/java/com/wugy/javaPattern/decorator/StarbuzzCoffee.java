package com.wugy.javaPattern.decorator;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.junit.Test;

/**
 * 星巴兹咖啡
 * 
 * @author wugy 2016年3月28日
 *
 */
public class StarbuzzCoffee {

	@Test
	public void testCoffee() {
		Beverage beverage = new Espresso();
		System.out.println(beverage.getDescription() + " $" + beverage.cost() + "\n");

		Beverage beverage1 = new DarkRoast();
		beverage1 = new Soy(beverage1);
		beverage1 = new Mocha(beverage1);
		beverage1 = new Whip(beverage1);
		System.out.println(beverage1.getDescription() + " $" + beverage1.cost() + "\n");

		Beverage beverage2 = new HouseBlend();
		beverage2 = new Soy(beverage2);
		beverage2 = new Mocha(beverage2);
		beverage2 = new Whip(beverage2);
		System.out.println(beverage2.getDescription() + " $" + beverage2.cost() + "\n");
	}

	@Test
	public void testInput() {
		int c;
		InputStream in = null;
		try {
			ClassLoader classLoader = getClass().getClassLoader();
			URL fileUrl = classLoader.getResource("test.txt");
			String file = fileUrl.getFile();
			in = new LowerCaseInputStream(new BufferedInputStream(new FileInputStream(file)));
			while ((c = in.read()) >= 0) {
				System.out.print((char) c);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
