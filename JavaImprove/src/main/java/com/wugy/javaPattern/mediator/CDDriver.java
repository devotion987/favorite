package com.wugy.javaPattern.mediator;

public class CDDriver extends Colleague {

	private String data = "";

	public CDDriver(Mediator mediator) {
		super(mediator);
	}

	public void readCD() {
		data = "设计模式,值得研究";
		getMidiator().changed(this);
	}

	public String getData() {
		return data;
	}

}
