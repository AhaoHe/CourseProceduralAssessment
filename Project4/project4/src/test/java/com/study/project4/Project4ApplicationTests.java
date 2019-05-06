package com.study.project4;

import com.study.project4.com.service.StudentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Project4ApplicationTests {

    @Autowired
    DataSource dataSource;
    @Autowired
    StudentService studentService=new StudentService();

    @Test
    public void contextLoads() throws SQLException {
        System.out.println(dataSource.getClass());

        Connection connection=dataSource.getConnection();
        System.out.println(connection);
        connection.close();
    }
    @Test
    public  void  login(){
        String password=studentService.getStuByid(2016051100).getSpsw();
        System.out.println(password);
    }

}
