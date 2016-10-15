package com.wugy.javaPattern.responsibilityChain.old;

public enum StateType {

	single(1, "未婚"), married(2, "已婚"), husbandDeath(3, "夫死"), unknown(-1, "未知");

	private int type;
	private String desc;

	private StateType(int type, String desc) {
		this.type = type;
		this.desc = desc;
	}

	public int getType() {
		return type;
	}

	public String getDesc() {
		return desc;
	}

	public static StateType getType(int type) {
		for (StateType stateType : StateType.values()) {
			if (stateType.getType() == type)
				return stateType;
		}
		return unknown;
	}

}
