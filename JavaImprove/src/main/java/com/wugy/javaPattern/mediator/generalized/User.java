package com.wugy.javaPattern.mediator.generalized;

public class User {

	private String userId;
	private String userName;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * 人员离职
	 * 
	 * @return
	 */
	public boolean dimisson() {
		DeptUserMediator mediator = DeptUserMediator.getInstance();
		mediator.deleteUser(userId);
		return true;
	}
}
