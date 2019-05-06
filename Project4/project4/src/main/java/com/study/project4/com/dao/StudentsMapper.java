package com.study.project4.com.dao;

import com.study.project4.com.entity.Student;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

//必须设置@Mapper在cProject4Application中设置@MapperScan扫描mapper文件
//在Mapper文件中用注解写SQL语句

public interface StudentsMapper {

    @Select("select * from Student")
    public List<Student> getStuAll();

    @Select("select * from student where id = #{id}")
    public  Student getStuByid(Integer id);

    @Options(useGeneratedKeys = true,keyProperty = "id")
    @Insert("insert into Student(sname,spsw,sclass) values(#{sname},#{spsw},#{sclass})")
    public  int insetStu(Student student);

}
