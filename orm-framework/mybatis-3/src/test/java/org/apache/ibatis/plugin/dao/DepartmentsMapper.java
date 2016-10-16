package org.apache.ibatis.plugin.dao;

import org.apache.ibatis.submitted.serializecircular.Department;

/**
 * Created by wugy on 2016/10/1.
 */
public interface DepartmentsMapper {

    int updateByPrimaryKey(Department department);

    Department selectByPrimaryKey(Integer departmentId);
}
