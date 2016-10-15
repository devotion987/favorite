package com.wugy.javaPattern.mediator.generalized;

public class Client {

	public static void main(String[] args) {
		DeptUserMediator mediator = DeptUserMediator.getInstance();

		Dept dept1 = new Dept(), dept2 = new Dept();
		User user1 = new User();

		dept1.setDeptId("deptId1");
		dept2.setDeptId("deptId2");
		user1.setUserId("userId1");

		System.out.println("------撤销部门前------");
		mediator.showUserDept(user1);
		dept1.deleteDept();
		System.out.println("------撤销部门后------");
		mediator.showUserDept(user1);

		System.out.println();
		System.out.println("------人员离职前------");
		mediator.showDeptUser(dept2);
		user1.dimisson();
		System.out.println("------人员离职后------");
		mediator.showDeptUser(dept2);
	}
}
