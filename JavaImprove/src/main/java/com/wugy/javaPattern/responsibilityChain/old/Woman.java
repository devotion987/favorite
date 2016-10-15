package com.wugy.javaPattern.responsibilityChain.old;

public class Woman implements IWomen {

	private StateType stateType;
	private String request = "";

	public Woman(int type, String request) {
		this.stateType = StateType.getType(type);
		this.request = request;
	}

	public Woman(StateType stateType, String request) {
		this.stateType = stateType;
		this.request = request;
	}

	@Override
	public int getType() {
		return stateType.getType();
	}

	@Override
	public String getRequest() {
		return request;
	}

}
