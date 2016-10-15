package com.wugy.tomcat.ex14.digestertest;

import java.util.ArrayList;
import java.util.List;

public class Employee {

	private String firstName;
	private String lastName;
	private List<Office> offices = new ArrayList<>();

	public Employee() {
		System.out.println("Creating Employee");
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		System.out.println("Setting firstName : " + firstName);
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		System.out.println("Setting lastName : " + lastName);
		this.lastName = lastName;
	}

	public void addOffice(Office office) {
		System.out.println("Adding Office to this employee");
		offices.add(office);
	}

	public List<Office> getOffices() {
		return offices;
	}

	public void printName() {
		System.out.println("My name is " + firstName + " " + lastName);
	}
}
