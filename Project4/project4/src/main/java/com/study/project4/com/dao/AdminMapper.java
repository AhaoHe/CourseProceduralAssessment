package com.study.project4.com.dao;

import com.study.project4.com.entity.*;
import org.apache.ibatis.annotations.*;

import java.util.List;

//必须设置@Mapper在cProject4Application中设置@MapperScan扫描mapper文件
//在Mapper文件中用注解写SQL语句

public interface AdminMapper {

    //登录系统，查询是否存在账户和密码
    @Select("select * from Admin where aid = #{aid}")
    public Admin getAdminByid(Integer id);

    //添加学生（studentMapper中）
    //查询所有学生学生（studentMapper中）

    //修改学生信息
    @Update("UPDATE student SET sname=#{sname},spsw=#{spsw},sclass=#{sclass},ssex=#{ssex}" +
            "WHERE id=#{id}")
    public int updateStu(Student student);
    //删除学生信息
    @Delete("DELETE FROM student WHERE id=#{id}")
    public int deleteStudent(Integer id);


    //删除老师信息
    @Delete("DELETE FROM teacher WHERE tid=#{id}")
    public int deleteTeacher(Integer id);

    //删除课程信息
    @Delete("DELETE FROM course WHERE cid=#{id}")
    public int deleteCourse(Integer id);

    //查询哪门课有哪些老师要删除哪些学生
    @Select("SELECT cs.cid,s.id,s.sname,s.sclass,s.ssex " +
            "FROM course_students cs,student s " +
            "WHERE cs.ifjoin=3 AND s.id=cs.id AND cs.cid=#{cid}")
    @Results({
            @Result(column = "id", property = "student",javaType = Student.class,
                    one = @One(select = "com.study.project4.com.dao.StudentsMapper.getStuByid")),
            @Result(column = "cid", property = "course",javaType = Course.class,
                    one = @One(select = "com.study.project4.com.dao.CourseMapper.getCourseByCid"))
    })
    public List<Course_Students> findTeaDelStuAll(Integer cid);

}
