package com.wugy.javaPattern.mediator.generalized;

import java.util.ArrayList;
import java.util.List;

/**
 * 实现部门和人员交互的实现类：只实现撤销部门和人员离职的功能
 * 
 * @author devotion
 *
 */
public class DeptUserMediator {

	private static DeptUserMediator instance = null;
	private List<DeptUserModel> deptUserList = new ArrayList<>();

	private DeptUserMediator() {
		initData();
	}

	public static DeptUserMediator getInstance() {
		if (null == instance)
			instance = new DeptUserMediator();
		return instance;
	}

	private void initData() {
		DeptUserModel model = new DeptUserModel();
		model.setDeptUserId("deptUserId1");
		model.setDeptId("deptId1");
		model.setUserId("userId1");
		deptUserList.add(model);

		model = new DeptUserModel();
		model.setDeptUserId("deptUserId2");
		model.setDeptId("deptId1");
		model.setUserId("userId2");
		deptUserList.add(model);

		model = new DeptUserModel();
		model.setDeptUserId("deptUserId3");
		model.setDeptId("deptId2");
		model.setUserId("userId3");
		deptUserList.add(model);

		model = new DeptUserModel();
		model.setDeptUserId("deptUserId4");
		model.setDeptId("deptId2");
		model.setUserId("userId4");
		deptUserList.add(model);

		model = new DeptUserModel();
		model.setDeptUserId("deptUserId5");
		model.setDeptId("deptId2");
		model.setUserId("userId1");
		deptUserList.add(model);
	}

	/**
	 * 撤销部门
	 * 
	 * @param deptId
	 * @return
	 */
	public boolean deleteDept(String deptId) {
		List<DeptUserModel> tempList = null;
		for (DeptUserModel model : deptUserList) {
			if (model.getDeptId().equals(deptId)) {
				tempList = new ArrayList<>();
				tempList.add(model);
			}
		}
		deptUserList.removeAll(tempList);
		return true;
	}

	/**
	 * 人员离职
	 * 
	 * @param userId
	 * @return
	 */
	public boolean deleteUser(String userId) {
		List<DeptUserModel> tempList = null;
		for (DeptUserModel model : deptUserList) {
			if (model.getUserId().equals(userId)) {
				tempList = new ArrayList<>();
				tempList.add(model);
			}
		}
		deptUserList.removeAll(tempList);
		return true;
	}

	/**
	 * 显示部门下所有人员
	 * 
	 * @param dept
	 *            部门对象
	 */
	public void showDeptUser(Dept dept) {
		for (DeptUserModel model : deptUserList) {
			if (dept.getDeptId().equals(model.getDeptId())) {
				System.out.println("部门编号：" + dept.getDeptId() + "下所有人员，其编号是：" + model.getUserId());
			}
		}
	}

	/**
	 * 显示人员所在部门
	 * 
	 * @param user
	 *            人员对象
	 */
	public void showUserDept(User user) {
		for (DeptUserModel model : deptUserList) {
			if (user.getUserId().equals(model.getUserId())) {
				System.out.println("人员编号：" + user.getUserId() + "，所在部门：" + model.getDeptId());
			}
		}
	}
}
