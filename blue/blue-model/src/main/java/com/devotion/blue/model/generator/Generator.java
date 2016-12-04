package com.devotion.blue.model.generator;

public class Generator {

	public static void main(String[] args) {
		
		String modelPackage = "io.jpress";
		
		String dbHost = "127.0.0.1";
		String dbName = "jpress";
		String dbUser = "root";
		String dbPassword = "";
		
		new JGenerator(modelPackage, dbHost, dbName, dbUser, dbPassword).doGenerate();

	}

}
