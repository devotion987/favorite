package org.apache.ibatis.plugin;

import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.Logger;
import org.junit.Test;

/**
 * SqlSession 简单查询演示类
 *
 * @author louluan
 */
public class CustomCachePluginTest {

    private static final Logger loger = Logger.getLogger(CustomCachePluginTest.class);

    @Test
    public void testPlugin() throws Exception {
        InputStream inputStream = Resources.getResourceAsStream("mybatisConfig.xml");
        SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
        SqlSessionFactory factory = builder.build(inputStream);

        SqlSession sqlSession = factory.openSession(true);
        SqlSession sqlSession2 = factory.openSession(true);
        //3.使用SqlSession查询
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("employeeId", 10);
        //a.查询工资低于10000的员工
        Date first = new Date();
        //第一次查询
        List<Employee> result = sqlSession.selectList("com.louis.mybatis.dao.EmployeesMapper.selectWithDepartments", params);
        sqlSession.commit();
        checkCacheStatus(sqlSession);
        params.put("employeeId", 11);
        result = sqlSession.selectList("com.louis.mybatis.dao.EmployeesMapper.selectWithDepartments", params);
        sqlSession.commit();
        checkCacheStatus(sqlSession);
        params.put("employeeId", 12);
        result = sqlSession.selectList("com.louis.mybatis.dao.EmployeesMapper.selectWithDepartments", params);
        sqlSession.commit();
        checkCacheStatus(sqlSession);
        params.put("employeeId", 13);
        result = sqlSession.selectList("com.louis.mybatis.dao.EmployeesMapper.selectWithDepartments", params);
        sqlSession.commit();
        checkCacheStatus(sqlSession);
        Department department = sqlSession.selectOne("com.louis.mybatis.dao.DepartmentsMapper.selectByPrimaryKey", 10);
        department.setDepartmentName("updated");
        sqlSession2.update("com.louis.mybatis.dao.DepartmentsMapper.updateByPrimaryKey", department);
        sqlSession.commit();
        checkCacheStatus(sqlSession);
    }


    public void checkCacheStatus(SqlSession sqlSession) {
        loger.info("------------Cache Status------------");
        Iterator<String> iter = sqlSession.getConfiguration().getCacheNames().iterator();
        while (iter.hasNext()) {
            String it = iter.next();
            loger.info(it + ":" + sqlSession.getConfiguration().getCache(it).getSize());
        }
        loger.info("------------------------------------");

    }

}
