package org.apache.ibatis.plugin.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.plugin.Employee;

/**
 * Created by Administrator on 2016/10/1.
 */
public interface EmployeesMapper {

    Employee selectByPrimaryKey(Integer employeeId);

    Employee selectWithDepartments(Integer employeeId);

    Employee selectByMinSalary(@Param("min_salary") float min_salary);
}
