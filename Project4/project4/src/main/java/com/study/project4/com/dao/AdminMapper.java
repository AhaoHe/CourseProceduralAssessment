package com.study.project4.com.dao;

import com.study.project4.com.entity.*;
import org.apache.ibatis.annotations.*;

//必须设置@Mapper在cProject4Application中设置@MapperScan扫描mapper文件
//在Mapper文件中用注解写SQL语句

public interface AdminMapper {

    /*
    *type：0,0,0  第一个代表考勤类型，第二个作业类型，第三个实验类型
    * */

    //登录系统，查询是否存在账户和密码
    @Select("select * from Admin where aid = #{aid}")
    public Admin getAdminByid(Integer id);

    //查询所有学生学生（studentMapper中）

    //添加学生（studentMapper中）
    @Options(useGeneratedKeys = true,keyProperty = "id")
    @Insert("insert into Student(sname,spsw,classid,ssex) " +
            "values(#{student.sname},#{student.spsw},#{student.className.classid},#{student.ssex})")
    public  int insetStu(@Param("student") Student student);

    //修改学生信息
    @Update("UPDATE student SET sname=#{student.sname},spsw=#{student.spsw},classid=#{student.className.classid},ssex=#{student.ssex} " +
            "WHERE id=#{student.id}")
    public int updateStu(@Param("student")Student student);
    //删除学生信息
    @Delete("DELETE FROM student WHERE id=#{id}")
    public int deleteStudent(Integer id);


    //删除老师信息
    @Delete("DELETE FROM teacher WHERE tid=#{id}")
    public int deleteTeacher(Integer id);
    //修改老师
    @Update("UPDATE teacher SET tname=#{tname},tpsw=#{tpsw},tsex=#{tsex} " +
            "WHERE tid=#{tid}")
    public int updateTeacher(Teacher teacher);


    //课程信息
    //删除课程信息
    @Delete("DELETE FROM course WHERE cid=#{id}")
    public int deleteCourse(Integer id);
    //添加课程信息
    @Options(useGeneratedKeys = true,keyProperty = "cid")
    @Insert("insert into course(course,tid,information,type) values(#{course.course},#{course.teacher.tid},#{course.information},#{course.type})")
    public  int insetCourse(@Param("course")Course course);
    //修改课程信息
    @Update("UPDATE course SET course=#{course.course},tid=#{course.teacher.tid},information=#{course.information},type=#{course.type} " +
            "WHERE cid=#{course.cid}")
    public int updateCourse(@Param("course")Course course);


    //删除班级信息
    @Delete("DELETE FROM classname WHERE classid=#{classid}")
    public int deleteClass(int classid);
    //添加班级信息
    @Options(useGeneratedKeys = true,keyProperty = "classid")
    @Insert("insert into classname(classname) values(#{className.classname})")
    public  int insetClass(@Param("className") ClassName className);
    //修改班级信息
    @Update("UPDATE classname SET classname=#{className.classname} " +
            "WHERE classid=#{className.classid}")
    public int updateClass(@Param("className") ClassName className);

    //导入学生
    @Insert("INSERT INTO course_students(cid,id,ifjoin) values(#{cid},#{id},2)")
    public  int importStudents(@Param("cid") int cid,@Param("id") int id);


    /*查询哪门课有哪些老师要删除哪些学生，已经被CouseMapper里的getStudentsAllByCid(int cid)代替
    @Select("SELECT cs.cid,s.id,s.sname,s.classid,s.ssex " +
            "FROM course_students cs,student s " +
            "WHERE cs.ifjoin=3 AND s.id=cs.id AND cs.cid=#{cid}")
    @Results({
            @Result(column = "id", property = "student",javaType = Student.class,
                    one = @One(select = "com.study.project4.com.dao.StudentsMapper.getStuByid")),
            @Result(column = "cid", property = "course",javaType = Course.class,
                    one = @One(select = "com.study.project4.com.dao.CourseMapper.getCourseByCid"))
    })
    public List<Course_Students> findTeaDelStuAll(Integer cid);*/

}
