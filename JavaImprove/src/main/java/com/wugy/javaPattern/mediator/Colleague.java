package com.wugy.javaPattern.mediator;

public abstract class Colleague {

	private Mediator mediator;

	public Colleague(Mediator mediator) {
		this.mediator = mediator;
	}

	public Mediator getMidiator() {
		return mediator;
	}

}
