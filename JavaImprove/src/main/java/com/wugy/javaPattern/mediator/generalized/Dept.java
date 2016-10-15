package com.wugy.javaPattern.mediator.generalized;

public class Dept {

	private String deptId;
	private String deptName;

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	/**
	 * 撤销部门
	 * 
	 * @return
	 */
	public boolean deleteDept() {
		DeptUserMediator mediator = DeptUserMediator.getInstance();
		mediator.deleteDept(deptId);
		return true;
	}

}
